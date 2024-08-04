package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.matchers.PairOfIsomorphicSet

internal class SubtreeSizeComparator : Comparator<PairOfIsomorphicSet> {
    override fun compare(
        pairOfSet1: PairOfIsomorphicSet,
        pairOfSet2: PairOfIsomorphicSet
    ): Int {
        // Since trees in mapping1 and mapping2 are all isomorphic,
        // We can only pick left(first) or right(second) to compare.
        val maxSubtreeSizeOfMapping1 = pairOfSet1.first.maxOf { it.subTreeSize }
        val maxSubtreeSizeOfMapping2 = pairOfSet2.first.maxOf { it.subTreeSize }
        return compareValues(maxSubtreeSizeOfMapping2, maxSubtreeSizeOfMapping1)
    }
}