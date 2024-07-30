package tw.xcc.gumtree.editscript

import kotlinx.coroutines.async
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

class SimplifiedEditScriptGenerator(
    private val tree1: GumTree,
    private val tree2: GumTree,
    private val storage: TreeMappingStorage<GumTree>
) : EditScriptGenerator {
    private val baseGenerator by lazy { ChawatheScriptGenerator(tree1, tree2, storage) }
    private val lock = Mutex()
    private val cachedDescendents = mutableMapOf<String, List<GumTree>>()

    private fun descendentsOf(tree: GumTree) = cachedDescendents.getOrPut(tree.id) { tree.descendents }

    private suspend inline fun <T : Action, N : Action> simplifyWith(
        actions: MutableList<Action>,
        nodeToIndexedSingleActions: Map<GumTree, Pair<Int, T>>,
        crossinline createTreeActionFrom: (origin: T) -> N
    ) = coroutineScope {
        val nodeAsKeys = nodeToIndexedSingleActions.keys
        for ((node, indexedSingleAction) in nodeToIndexedSingleActions.entries) {
            val nodeParent = node.getParent() ?: continue
            val (indexInOriginActions, originAction) = indexedSingleAction

            if (nodeAsKeys.contains(nodeParent) && nodeAsKeys.containsAll(descendentsOf(nodeParent))) {
                lock.withLock {
                    actions.removeAt(indexInOriginActions)
                }
            } else if (node.childCount() > 0 && nodeAsKeys.containsAll(descendentsOf(node))) {
                val treeAction = createTreeActionFrom(originAction)
                lock.withLock {
                    actions.add(indexInOriginActions, treeAction)
                    actions.removeAt(indexInOriginActions + 1)
                }
            }
        }
    }

    override suspend fun generateActions(): List<Action> =
        coroutineScope {
            val actions = baseGenerator.generateActions().toMutableList()

            val simplifiableActions =
                actions
                    .flatMapIndexed { originIdx, action ->
                        when (action) {
                            is SingleInsertAction -> listOf(originIdx to action)
                            is SingleDeleteAction -> listOf(originIdx to action)
                            else -> emptyList()
                        }
                    }

            val nodeToIndexedSingleInsertActionJob =
                async {
                    simplifiableActions
                        .filterIsInstance<Pair<Int, SingleInsertAction>>()
                        .associateBy { it.second.node }
                }
            val nodeToIndexedSingleDeleteActionJob =
                async {
                    simplifiableActions
                        .filterIsInstance<Pair<Int, SingleDeleteAction>>()
                        .associateBy { it.second.node }
                }

            val insertionSimplifyJob =
                launch {
                    val nodeToIndexedSingleInsertActions = nodeToIndexedSingleInsertActionJob.await()
                    simplifyWith(actions, nodeToIndexedSingleInsertActions) { originAction ->
                        TreeInsertAction(originAction.node, originAction.parent, originAction.pos)
                    }
                }

            val deletionSimplifyJob =
                launch {
                    val nodeToIndexedSingleDeleteActions = nodeToIndexedSingleDeleteActionJob.await()
                    simplifyWith(actions, nodeToIndexedSingleDeleteActions) { originAction ->
                        TreeDeleteAction(originAction.node)
                    }
                }

            listOf(insertionSimplifyJob, deletionSimplifyJob).joinAll()
            return@coroutineScope actions
        }
}