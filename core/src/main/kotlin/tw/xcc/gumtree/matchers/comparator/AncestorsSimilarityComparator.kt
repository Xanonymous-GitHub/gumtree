package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.algorithms.lcsBaseOnlySize
import tw.xcc.gumtree.model.GumTree

class AncestorsSimilarityComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun findLCSOfTreeLists(
        trees1: List<GumTree>,
        trees2: List<GumTree>
    ): Int =
        lcsBaseOnlySize(trees1, trees2) { tree1, tree2 ->
            tree1 hasSameTypeAs tree2
        }

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        if (hasSameParent(m1, m2)) {
            return 0
        }

        val ancestorsOfM1L = m1.first.ancestors
        val ancestorsOfM1R = m1.second.ancestors
        val ancestorsOfM2L = m2.first.ancestors
        val ancestorsOfM2R = m2.second.ancestors

        val similarityOfM1 =
            calculateDiceValue(
                findLCSOfTreeLists(ancestorsOfM1L, ancestorsOfM1R),
                ancestorsOfM1L.size,
                ancestorsOfM1R.size
            )

        val similarityOfM2 =
            calculateDiceValue(
                findLCSOfTreeLists(ancestorsOfM2L, ancestorsOfM2R),
                ancestorsOfM2L.size,
                ancestorsOfM2R.size
            )

        return compareValues(similarityOfM2, similarityOfM1)
    }
}