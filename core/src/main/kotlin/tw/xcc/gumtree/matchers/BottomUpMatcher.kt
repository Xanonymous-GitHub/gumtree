package tw.xcc.gumtree.matchers

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import tw.xcc.gumtree.algorithms.lcsBaseWithElements
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.api.tree.TreeMatcher
import tw.xcc.gumtree.matchers.comparator.numOfMappedDescendents
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import tw.xcc.gumtree.model.NonFrozenGumTreeCachePool
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.ln

class BottomUpMatcher : TreeMatcher<GumTree> {
    private val realTreePool = NonFrozenGumTreeCachePool

    private suspend fun lcsFinalMatching(
        tree1UnMappedChildren: List<GumTree>,
        tree2UnMappedChildren: List<GumTree>,
        storage: TreeMappingStorage<GumTree>,
        equalFunc: suspend (GumTree, GumTree) -> Boolean
    ) = coroutineScope {
        val lcsOfUnMapped =
            lcsBaseWithElements(tree1UnMappedChildren, tree2UnMappedChildren) {
                    left, right ->
                equalFunc(left, right)
            }

        lcsOfUnMapped.forEach { (left, right) ->
            val areAllLeftSideUnmapped =
                async {
                    left.preOrdered().all { !storage.isLeftMapped(it) }
                }
            val areAllRightSideUnmapped =
                async {
                    right.preOrdered().all { !storage.isRightMapped(it) }
                }
            if (areAllLeftSideUnmapped.await() && areAllRightSideUnmapped.await()) {
                storage.addMappingRecursivelyOf(
                    realTreePool.tryExtractRealOf(left to right)
                )
            }
        }
    }

    private suspend fun histogramMatching(
        tree1UnMappedChildren: List<GumTree>,
        tree2UnMappedChildren: List<GumTree>,
        storage: TreeMappingStorage<GumTree>
    ) = coroutineScope {
        val histogramOfLeft = tree1UnMappedChildren.groupBy { it.info.type }
        val histogramOfRight = tree2UnMappedChildren.groupBy { it.info.type }

        (histogramOfLeft.keys intersect histogramOfRight.keys).forEach { key ->
            val leftNodes = histogramOfLeft[key]
            val rightNodes = histogramOfRight[key]
            if ((leftNodes != null && rightNodes != null) && (leftNodes.size == 1 && rightNodes.size == 1)) {
                val left = leftNodes.single()
                val right = rightNodes.single()
                storage.addMappingOf(
                    realTreePool.tryExtractRealOf(left to right)
                )
                postMatching(left, right, storage)
            }
        }
    }

    private suspend fun postMatching(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ) {
        coroutineScope {
            val unMappedChildrenOfLeftJob =
                async {
                    tree1.getChildren().filter { !storage.isLeftMapped(it) }
                }
            val unMappedChildrenOfRightJob =
                async {
                    tree2.getChildren().filter { !storage.isRightMapped(it) }
                }

            val unMappedChildrenOfLeft = unMappedChildrenOfLeftJob.await()
            val unMappedChildrenOfRight = unMappedChildrenOfRightJob.await()

            lcsFinalMatching(unMappedChildrenOfLeft, unMappedChildrenOfRight, storage) { left, right ->
                left isIsomorphicTo right
            }

            lcsFinalMatching(unMappedChildrenOfLeft, unMappedChildrenOfRight, storage) { left, right ->
                left isIsoStructuralTo right
            }

            histogramMatching(unMappedChildrenOfLeft, unMappedChildrenOfRight, storage)
        }
    }

    private fun extractCandidatesOf(
        left: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): List<GumTree> {
        val mappedRights =
            left.descendents.mapNotNull {
                storage.getMappingOfLeft(it)
            }

        val candidates = mutableListOf<GumTree>()
        val visited = mutableSetOf<GumTree>()
        mappedRights.forEach {
            val tracingNode = AtomicReference(it)
            while (!tracingNode.get().isRoot()) {
                val parent = tracingNode.get().getParent()!!
                if (visited.contains(parent)) {
                    break
                }

                visited.add(parent)
                if (parent.info.type == left.info.type && !storage.isRightMapped(parent) && !parent.isRoot()) {
                    candidates.add(parent)
                }

                tracingNode.set(parent)
            }
        }

        return candidates
    }

    private fun calculateChawatheSimilarity(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): Double {
        val maxSubTreeSize = maxOf(tree1.subTreeSize, tree2.subTreeSize)
        return numOfMappedDescendents(tree1, tree2, storage).toDouble() / maxSubTreeSize.toDouble()
    }

    override suspend fun match(
        tree1: GumTree,
        tree2: GumTree
    ): TreeMappingStorage<GumTree> = match(tree1, tree2, MappingStorage())

    override suspend fun match(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): TreeMappingStorage<GumTree> =
        coroutineScope {
            for (t in tree1.postOrdered()) {
                when {
                    t.isRoot() -> {
                        storage.addMappingOf(
                            realTreePool.tryExtractRealOf(t to tree2)
                        )
                        postMatching(t, tree2, storage)
                        break
                    }
                    !storage.isLeftMapped(t) && !t.isLeaf() -> {
                        val candidates = extractCandidatesOf(t, storage)
                        val subTreeSize = t.subTreeSize.toDouble()

                        val bestSimilarCandidate =
                            candidates.maxByOrNull { candidate ->
                                val threshold = 1.0 / (1.0 + ln(candidate.subTreeSize.toDouble() + subTreeSize))
                                val similarity = calculateChawatheSimilarity(t, candidate, storage)
                                similarity.takeIf { it >= threshold } ?: 0.0
                            }

                        if (bestSimilarCandidate != null) {
                            postMatching(t, bestSimilarCandidate, storage)
                            storage.addMappingOf(
                                realTreePool.tryExtractRealOf(t to bestSimilarCandidate)
                            )
                        }
                    }
                    storage.isLeftMapped(t) && storage.hasUnMappedDescendentOfLeft(t) -> {
                        val mappedRight = storage.getMappingOfLeft(t)
                        if (mappedRight == null || !storage.hasUnMappedDescendentOfRight(mappedRight)) {
                            continue
                        }

                        postMatching(t, mappedRight, storage)
                    }
                }
            }

            return@coroutineScope storage
        }
}