package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.model.GumTree
import kotlin.math.sqrt

internal class PositionOfAncestorsSimilarityComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun collectPositionsOfAncestorsFrom(tree: GumTree): List<Int> {
        val positions = mutableListOf<Int>()
        positions.add(tree.positionOfParent)
        positions.addAll(tree.ancestors.map { it.positionOfParent }.dropLast(1))
        return positions
    }

    private fun findDistanceOfMapping(m: Pair<GumTree, GumTree>): Double {
        val positionsOfParentsOfM1 = collectPositionsOfAncestorsFrom(m.first)
        val positionsOfParentsOfM2 = collectPositionsOfAncestorsFrom(m.second)

        val smallerSize = minOf(positionsOfParentsOfM1.size, positionsOfParentsOfM2.size)

        val sum =
            (0 until smallerSize).sumOf {
                (positionsOfParentsOfM1[it] - positionsOfParentsOfM2[it]).let { pos -> pos * pos }
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