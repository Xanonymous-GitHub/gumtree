package tw.xcc.gumtree.editscript

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import tw.xcc.gumtree.algorithms.lcsBaseWithElements
import tw.xcc.gumtree.api.EditScriptGenerator
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.helper.bfsOrderOf
import tw.xcc.gumtree.helper.crossProductOf
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import tw.xcc.gumtree.model.operations.Action
import tw.xcc.gumtree.model.operations.SingleDeleteAction
import tw.xcc.gumtree.model.operations.SingleInsertAction
import tw.xcc.gumtree.model.operations.SingleUpdateAction
import tw.xcc.gumtree.model.operations.TreeMoveAction

/**
 * An edit-script generator that aims to find the minimum-cost operation collections,
 * which lets [tree1] transform to another new tree that is isomorphic to [tree2].
 * */
class ChawatheScriptGenerator(
    private val tree1: GumTree,
    private val tree2: GumTree,
    private val storage: TreeMappingStorage<GumTree>
) : EditScriptGenerator {
    private val actions = mutableListOf<Action>()
    private val leftReadyNodes = mutableSetOf<GumTree>()
    private val rightReadyNodes = mutableSetOf<GumTree>()
    private val fakeTreeTextPattern by lazy { "_FAKED(${hashCode()})*" }

    /**
     * To generate a fake [GumTree] node.
     * The used [fakeTreeTextPattern] is a string that will directly be converted to [Regex] for validation.
     * @see ensureTreeNotUsed
     * */
    private fun generateNewEmptyNode() = GumTree(GumTree.Info.of(text = fakeTreeTextPattern))

    /**
     * To make sure the root of both given left ([tree1]) and right ([tree2]) haven't been
     * used for calculating the edit script.
     * This step is used to prevent multiple fake tree nodes being appended to the same tree,
     * which can potentially lead to a wrong result.
     * */
    private fun ensureTreeNotUsed() {
        val pattern = fakeTreeTextPattern.toRegex()
        require(
            arrayOf(tree1, tree2).all {
                !it.info.text.matches(pattern)
            }
        ) { "Trees can not be reused for generating edit scripts" }
    }

    /**
     * To find a location in the left tree ([tree1]) that is appropriate to insert (or move) a node to.
     * The function takes [targetOnRight] as the standard, finding the position in left side
     * that can be placed with a node so their parents can become isomorphic with each other.
     * This function may not always return an exactly expected position. If that happened,
     * a zero number will be returned, but expect [alignChildrenOf] to fix the order of nodes.
     * */
    private fun findInsertionPosFrom(targetOnRight: GumTree): Int {
        val parentOfRightTarget = targetOnRight.getParent() ?: return 0
        val siblings = parentOfRightTarget.getChildren()

        if (siblings.firstOrNull { rightReadyNodes.contains(it) } == targetOnRight) {
            return 0
        }

        val targetIdxInSiblings = siblings.indexOf(targetOnRight)
        require(targetIdxInSiblings != -1) { "targetOnRight should exist in its sibling list" }

        val rightMostReadySibling =
            siblings
                .subList(0, targetIdxInSiblings)
                .lastOrNull { rightReadyNodes.contains(it) }
                ?: return 0

        val partnerOfRightMostReadySibling =
            storage
                .getMappingOfRight(rightMostReadySibling) ?: return 0

        return partnerOfRightMostReadySibling.positionOfParent + 1
    }

    /**
     * Performs a real insert operation for [leftNode] on [leftParentForInsert],
     * and pushes a [SingleInsertAction] to [actions].
     * The given [referencedRightNode] is used for calculating where should [leftNode] be inserted.
     * And the inserted [leftNode] will be marked as mapped with [referencedRightNode].
     * @see findInsertionPosFrom
     * */
    private fun performInsertActionFor(
        leftNode: GumTree,
        leftParentForInsert: GumTree,
        referencedRightNode: GumTree
    ) {
        val insertPosition = findInsertionPosFrom(referencedRightNode)
        leftParentForInsert.insertChildAt(insertPosition, leftNode)
        storage.addMappingOf(leftNode to referencedRightNode)
        actions.add(SingleInsertAction(referencedRightNode, leftParentForInsert, insertPosition))
    }

    /**
     * Performs a real update operation on [leftNode], and pushes a [SingleUpdateAction] to [actions].
     * */
    private fun performUpdateActionFor(
        leftNode: GumTree,
        newType: TreeType,
        newText: String
    ) {
        leftNode.info = leftNode.info.copy(type = newType, text = newText)
        actions.add(SingleUpdateAction(leftNode, newType, newText))
    }

    /**
     * Performs a real move operation on [leftParentOfMove], and pushes a [TreeMoveAction] to [actions].
     * A [referencedRightNode] will be used only for calculating the move destination location of [leftNode].
     * The [leftNode] will be disconnected from its parent, then re-insert to somewhere of [leftParentOfMove].
     * If the origin parent of [leftNode] is not [leftParentOfMove], an [AssertionError] will be thrown.
     * */
    private fun performMoveActionFor(
        leftNode: GumTree,
        leftParentOfMove: GumTree,
        referencedRightNode: GumTree
    ) {
        val destinationPosition = findInsertionPosFrom(referencedRightNode)
        check(leftNode.leaveParent()) { "The left node should be correctly removed from its parent" }
        leftParentOfMove.insertChildAt(destinationPosition, leftNode)
        actions.add(
            TreeMoveAction(
                leftNode,
                leftParentOfMove,
                destinationPosition,
                referencedRightNode.info.line,
                referencedRightNode.info.posOfLine
            )
        )
    }

    /**
     * Performs a real delete operation on [leftNode], and pushes a [SingleDeleteAction] to [actions].
     * We expect to put a cloned node of [leftNode] into [actions],
     * because the deleted node lost its relationship with the original tree, that causes
     * the tree-delete action cannot be recognized.
     * */
    private fun performDeleteActionOf(
        leftNode: GumTree,
        leftNodeFromClonedTree: GumTree
    ) {
        check(leftNode.leaveParent()) { "The leftNode should be correctly removed from its parent" }
        actions.add(SingleDeleteAction(leftNodeFromClonedTree))
    }

    /**
     * Re-order the mapped children of [pair] (which is expected a matching in [storage]),
     * so that these mapped-children can have same index of their parents.
     * */
    private suspend fun alignChildrenOf(pair: Pair<GumTree, GumTree>) =
        coroutineScope {
            val childrenOfLeft = pair.first.getChildren()
            val childrenOfRight = pair.second.getChildren()

            val clearReadyChildrenJob =
                launch {
                    leftReadyNodes.removeAll(childrenOfLeft.toSet())
                    rightReadyNodes.removeAll(childrenOfRight.toSet())
                }

            val mappedLChildrenToRJob =
                async {
                    childrenOfLeft.filter {
                        childrenOfRight.contains(storage.getMappingOfLeft(it))
                    }
                }
            val mappedRChildrenToLJob =
                async {
                    childrenOfRight.filter {
                        childrenOfLeft.contains(storage.getMappingOfRight(it))
                    }
                }

            val mappedLChildrenToR = mappedLChildrenToRJob.await()
            val mappedRChildrenToL = mappedRChildrenToLJob.await()

            val lcsOfMappedChildren =
                lcsBaseWithElements(mappedLChildrenToR, mappedRChildrenToL) { left, right ->
                    storage.has(left to right)
                }

            clearReadyChildrenJob.join()

            lcsOfMappedChildren.forEach { (left, right) ->
                leftReadyNodes.add(left)
                rightReadyNodes.add(right)
            }

            (mappedLChildrenToR crossProductOf mappedRChildrenToL).forEach { children ->
                if (storage.has(children) && !lcsOfMappedChildren.contains(children)) {
                    performMoveActionFor(children.first, pair.first, children.second)
                    leftReadyNodes.add(children.first)
                    rightReadyNodes.add(children.second)
                }
            }
        }

    override suspend fun generateActions(): List<Action> =
        coroutineScope {
            ensureTreeNotUsed()
            actions.clear()

            // Since the root of tree1(left) and tree2(right) may not match,
            // we directly add a matched Fake node pair to the storage
            // without considering whether they(the roots) are actually matched or not, and
            // let these two fake nodes to be the new root of tree1 and tree2.
            val fakeLeftRoot = generateNewEmptyNode()
            val fakeRightRoot = generateNewEmptyNode()
            tree1.setParentTo(fakeLeftRoot)
            tree2.setParentTo(fakeRightRoot)
            storage.addMappingOf(fakeLeftRoot to fakeRightRoot)

            bfsOrderOf(tree2) continuable@{ rightTarget ->
                val parentOfRightTarget = requireNotNull(rightTarget.getParent()) { "Parent node should always exist" }
                val partnerOfParentOfRightTarget = storage.getMappingOfRight(parentOfRightTarget)

                val leftTarget: GumTree

                if (!storage.isRightMapped(rightTarget) && partnerOfParentOfRightTarget != null) {
                    leftTarget = generateNewEmptyNode()
                    leftTarget.info =
                        leftTarget.info.copy(
                            type = rightTarget.info.type.copy(),
                            text = rightTarget.info.text
                        )
                    performInsertActionFor(leftTarget, partnerOfParentOfRightTarget, rightTarget)
                } else {
                    leftTarget =
                        requireNotNull(storage.getMappingOfRight(rightTarget)) {
                            "rightTarget should have partner here"
                        }

                    if (rightTarget != tree2) {
                        if (
                            leftTarget.info.text != rightTarget.info.text ||
                            leftTarget.info.type != rightTarget.info.type
                        ) {
                            performUpdateActionFor(leftTarget, rightTarget.info.type.copy(), rightTarget.info.text)
                        }

                        val parentOfLeftTarget = leftTarget.getParent()
                        if (
                            parentOfLeftTarget != null &&
                            partnerOfParentOfRightTarget != null &&
                            partnerOfParentOfRightTarget != parentOfLeftTarget
                        ) {
                            performMoveActionFor(leftTarget, partnerOfParentOfRightTarget, rightTarget)
                        }
                    }
                }

                leftReadyNodes.add(leftTarget)
                rightReadyNodes.add(rightTarget)
                alignChildrenOf(leftTarget to rightTarget)
            }

            // The DELETE phrase, which is the last step of the Chawathe algorithm.
            // It finds nodes that are on the left side but not on the right side.
            // After finishing prior steps, the remaining nodes compared to the tree on the right
            // are considered to be transformed to the right only through "deletion".
            val postOrderedClonedTree1Nodes = async { GumTree(tree1).postOrdered() }
            val postOrderedTree1Nodes = async { tree1.postOrdered() }
            postOrderedTree1Nodes.await().zip(postOrderedClonedTree1Nodes.await())
                .forEach { (nodeOnLeft, cloned) ->
                    if (!storage.isLeftMapped(nodeOnLeft)) {
                        performDeleteActionOf(nodeOnLeft, cloned)
                    }
                }
            return@coroutineScope actions
        }
}