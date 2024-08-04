package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.model.GumTree
import kotlin.math.abs

internal class TextualPositionDistanceComparator : Comparator<Pair<GumTree, GumTree>> {
    private fun calculateTexturalPositionDistanceOf(
        t1: GumTree,
        t2: GumTree
    ): Int =
        abs(
            t1.info.posOfLine - t2.info.posOfLine
        ) +
            abs(
                (t1.info.posOfLine + t1.info.text.length) - (t2.info.posOfLine + t2.info.text.length)
            )

    override fun compare(
        m1: Pair<GumTree, GumTree>,
        m2: Pair<GumTree, GumTree>
    ): Int {
        val m1PositionDistance = calculateTexturalPositionDistanceOf(m1.first, m1.second)
        val m2PositionDistance = calculateTexturalPositionDistanceOf(m2.first, m2.second)

        return compareValues(m1PositionDistance, m2PositionDistance)
    }
}