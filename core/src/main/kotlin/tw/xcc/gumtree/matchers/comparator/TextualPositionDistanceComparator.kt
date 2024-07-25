package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.model.GumTree
import kotlin.math.abs

class TextualPositionDistanceComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun calculateTexturalPositionDistanceOf(
        t1: GumTree,
        t2: GumTree
    ): Int = abs(t1.pos - t2.pos) + abs((t1.pos + t1.length) - (t2.pos + t2.length))

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        val m1PositionDistance = calculateTexturalPositionDistanceOf(m1.first, m1.second)
        val m2PositionDistance = calculateTexturalPositionDistanceOf(m2.first, m2.second)

        return compareValues(m1PositionDistance, m2PositionDistance)
    }
}