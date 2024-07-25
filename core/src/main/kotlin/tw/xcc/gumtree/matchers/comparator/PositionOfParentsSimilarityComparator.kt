package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.model.GumTree
import kotlin.math.sqrt

class PositionOfParentsSimilarityComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun collectPositionsOfParents(tree: GumTree): List<Int> {
        val positionOfParents = mutableListOf<Int>()
        var current = tree
        while (!current.isRoot()) {
            positionOfParents.add(current.positionOfParent)
            current = current.getParent()!!
        }
        return positionOfParents
    }

    private fun findDistanceOfMapping(m: Pair<GumTree, GumTree>): Double {
        val positionsOfParentsOfM1 = collectPositionsOfParents(m.first)
        val positionsOfParentsOfM2 = collectPositionsOfParents(m.second)

        val smallerSize = minOf(positionsOfParentsOfM1.size, positionsOfParentsOfM2.size)

        var sum = 0
        for (i in 0 until smallerSize) {
            sum += (positionsOfParentsOfM1[i] - positionsOfParentsOfM2[i]).let { it * it }
        }

        return sqrt(sum.toDouble())
    }

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        val distanceOfM1 = findDistanceOfMapping(m1)
        val distanceOfM2 = findDistanceOfMapping(m2)
        return compareValues(distanceOfM1, distanceOfM2)
    }
}