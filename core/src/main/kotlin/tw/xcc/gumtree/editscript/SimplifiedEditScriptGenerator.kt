package tw.xcc.gumtree.editscript

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tw.xcc.gumtree.api.EditScriptGenerator
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.operations.Action
import tw.xcc.gumtree.model.operations.SingleDeleteAction
import tw.xcc.gumtree.model.operations.SingleInsertAction
import tw.xcc.gumtree.model.operations.TreeDeleteAction
import tw.xcc.gumtree.model.operations.TreeInsertAction
import java.util.Optional

/**
 * A second-stage edit script generator that simplifies the edit script generated by the [ChawatheScriptGenerator].
 * It removes redundant single insertions and deletions and replaces them with tree insertions and deletions.
 * That means if a node is inserted and all of its descendents are also inserted, the single insertions will be
 * replaced with a tree insertion of their parent.
 * The same goes for deletions.
 * */
class SimplifiedEditScriptGenerator(
    private val tree1: GumTree,
    private val tree2: GumTree,
    private val storage: TreeMappingStorage<GumTree>
) : EditScriptGenerator {
    private val baseGenerator by lazy { ChawatheScriptGenerator(tree1, tree2, storage) }
    private val lock = Mutex()
    private val cachedDescendents = mutableMapOf<String, List<GumTree>>()

    /**
     * Retrieve the descendents of a tree node.
     * If the descendents are already cached, return the cached value.
     * Otherwise, calculate the descendents and cache them.
     * */
    private fun descendentsOf(tree: GumTree) = cachedDescendents.getOrPut(tree.id) { tree.descendents }

    /**
     * Replace single tree [Action] with another tree [Action]
     * if the node and all of its descendents are also in the [flatActions] list.
     * */
    private suspend inline fun <T : Action, N : Action> simplifyWith(
        flatActions: MutableList<Optional<Action>>,
        nodeToIndexedSingleActions: Map<GumTree, Pair<Int, T>>,
        crossinline createTreeActionFrom: (origin: T) -> N
    ) = coroutineScope {
        val nodeAsKeys = nodeToIndexedSingleActions.keys
        for ((node, indexedSingleAction) in nodeToIndexedSingleActions.entries) {
            val nodeParent = node.getParent() ?: continue
            val (indexInOriginActions, originAction) = indexedSingleAction

            if (nodeAsKeys.contains(nodeParent) && nodeAsKeys.containsAll(descendentsOf(nodeParent))) {
                lock.withLock {
                    flatActions[indexInOriginActions] = Optional.empty()
                }
            } else if (!node.isLeaf() && nodeAsKeys.containsAll(descendentsOf(node))) {
                val treeAction = createTreeActionFrom(originAction)
                lock.withLock {
                    flatActions[indexInOriginActions] = Optional.of(treeAction)
                }
            }
        }
    }

    override suspend fun generateActions(): List<Action> =
        coroutineScope {
            val actions =
                baseGenerator.generateActions()
                    .map { Optional.of(it) }
                    .toMutableList()

            val nodeToIndexedSingleInsertActions = mutableMapOf<GumTree, Pair<Int, SingleInsertAction>>()
            val nodeToIndexedSingleDeleteActions = mutableMapOf<GumTree, Pair<Int, SingleDeleteAction>>()

            actions.forEachIndexed { idx, it ->
                when (val action = it.get()) {
                    is SingleInsertAction -> {
                        nodeToIndexedSingleInsertActions[action.node] = idx to action
                    }
                    is SingleDeleteAction -> {
                        nodeToIndexedSingleDeleteActions[action.node] = idx to action
                    }
                }
            }

            val insertionSimplifyJob =
                launch {
                    simplifyWith(actions, nodeToIndexedSingleInsertActions) { originAction ->
                        TreeInsertAction(originAction.node, originAction.parent, originAction.pos)
                    }
                }

            val deletionSimplifyJob =
                launch {
                    simplifyWith(actions, nodeToIndexedSingleDeleteActions) { originAction ->
                        TreeDeleteAction(originAction.node)
                    }
                }

            listOf(insertionSimplifyJob, deletionSimplifyJob).joinAll()
            return@coroutineScope actions.filter { it.isPresent }.map { it.get() }
        }
}