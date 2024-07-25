package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.model.GumTree

class SiblingsSimilarityComparator(
    private val storage: TreeMappingStorage<GumTree>
) : Comparator<Pair<GumTree, GumTree>> {
    private fun sizeOfCommonDescendentsOf(
        p1: GumTree?,
        p2: GumTree?
    ): Int {
        if (p1 == null || p2 == null) {
            return 0
        }

        var commonCount = 0
        p1.descendents.forEach {
            val mappedR = storage.getMappingOfLeft(it)
            if (mappedR != null && mappedR in p2.descendents) {
                commonCount++
            }
        }

        return commonCount
    }

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        if (hasSameParent(m1, m2)) {
            return 0
        }

        val similarityOfM1 =
            calculateDiceValue(
                sizeOfCommonDescendentsOf(m1.first.getParent(), m1.second.getParent()),
                m1.first.getParent()?.descendents?.size ?: 0,
                m1.second.getParent()?.descendents?.size ?: 0
            )

        val similarityOfM2 =
            calculateDiceValue(
                sizeOfCommonDescendentsOf(m2.first.getParent(), m2.second.getParent()),
                m2.first.getParent()?.descendents?.size ?: 0,
                m2.second.getParent()?.descendents?.size ?: 0
            )

        return compareValues(similarityOfM2, similarityOfM1)
    }
}