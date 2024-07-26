package tw.xcc.gumtree.matchers

import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.api.tree.TreeMatcher
import tw.xcc.gumtree.matchers.algorithms.lcsBaseWithElements
import tw.xcc.gumtree.matchers.comparator.numOfMappedDescendents
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.ln

class BottomUpMatcher : TreeMatcher<GumTree> {
    private fun lcsFinalMatching(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>,
        equalFunc: (GumTree, GumTree) -> Boolean
    ) {
        val unMappedChildrenOfLeft = tree1.getChildren().filter { !storage.isLeftMapped(it) }
        val unMappedChildrenOfRight = tree2.getChildren().filter { !storage.isRightMapped(it) }

        val lcsOfUnMapped =
            lcsBaseWithElements(unMappedChildrenOfLeft, unMappedChildrenOfRight, equalFunc)

        lcsOfUnMapped.forEach { (left, right) ->
            val areAllLeftSideUnmapped = left.preOrdered().all { !storage.isLeftMapped(it) }
            val areAllRightSideUnmapped = right.preOrdered().all { !storage.isRightMapped(it) }
            if (areAllLeftSideUnmapped && areAllRightSideUnmapped) {
                storage.addMappingRecursivelyOf(left to right)
            }
        }
    }

    private fun histogramMatching(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ) {
        val histogramOfLeft = tree1.getChildren().filter { !storage.isLeftMapped(it) }.groupBy { it.type }
        val histogramOfRight = tree2.getChildren().filter { !storage.isRightMapped(it) }.groupBy { it.type }

        (histogramOfLeft.keys intersect histogramOfRight.keys).forEach { key ->
            val leftNodes = histogramOfLeft[key]
            val rightNodes = histogramOfRight[key]
            if ((leftNodes != null && rightNodes != null) && (leftNodes.size == 1 && rightNodes.size == 1)) {
                val left = leftNodes.single()
                val right = rightNodes.single()
                storage.addMappingOf(left to right)
                postMatching(left, right, storage)
            }
        }
    }

    private fun postMatching(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ) {
        lcsFinalMatching(tree1, tree2, storage) { left, right ->
            left isIsomorphicTo right
        }
        lcsFinalMatching(tree1, tree2, storage) { left, right ->
            left isIsoStructuralTo right
        }

        histogramMatching(tree1, tree2, storage)
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
                if (parent.type == left.type && !storage.isRightMapped(parent) && !parent.isRoot()) {
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

    override fun match(
        tree1: GumTree,
        tree2: GumTree
    ): TreeMappingStorage<GumTree> = match(tree1, tree2, MappingStorage())

    override fun match(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): TreeMappingStorage<GumTree> {
        for (t in tree1.postOrdered()) {
            when {
                t.isRoot() -> {
                    storage.addMappingOf(t to tree2)
                    postMatching(t, tree2, storage)
                    break
                }
                !storage.isLeftMapped(t) && !t.isLeaf() -> {
                    val candidates = extractCandidatesOf(t, storage)
                    val subTreeSize = t.subTreeSize.toDouble()

                    val bestSimilarCandidate =
                        candidates.maxByOrNull { candidate ->
                            val similarityThreshold = 1.0 / (1.0 + ln(candidate.subTreeSize.toDouble() + subTreeSize))
                            val similarity = calculateChawatheSimilarity(t, candidate, storage)
                            similarity.takeIf { it >= similarityThreshold } ?: 0.0
                        }

                    if (bestSimilarCandidate != null) {
                        postMatching(t, bestSimilarCandidate, storage)
                        storage.addMappingOf(t to bestSimilarCandidate)
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

        return storage
    }
}