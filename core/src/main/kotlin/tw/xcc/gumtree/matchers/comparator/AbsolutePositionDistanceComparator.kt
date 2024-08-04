package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.model.GumTree
import kotlin.math.abs

internal class AbsolutePositionDistanceComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun calculateDistanceOf(
        t1: GumTree,
        t2: GumTree
    ): Int = abs(t1.positionOfParent - t2.positionOfParent)

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        val distanceOfM1 = calculateDistanceOf(m1.first, m1.second)
        val distanceOfM2 = calculateDistanceOf(m2.first, m2.second)
        return compareValues(distanceOfM1, distanceOfM2)
    }
}