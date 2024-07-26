package tw.xcc.gumtree.matchers.comparator

import tw.xcc.gumtree.api.tree.Tree
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.model.BasicTree
import kotlin.math.max

/**
 * The Dice function calculates the similarity between two sets.
 * The formula is 2 * |A ∩ B| / (|A| + |B|)
 * [left] and [right] are the sizes of the two sets.
 * [common] is the size of their intersections.
 *
 * If the size of the intersection is 0, the function returns 0.
 * And if both [left] and [right] are 0, the function returns 1.
 * Because the two sets are the same (empty).
 * */
fun calculateDiceValue(
    common: Long,
    left: Long,
    right: Long
): Double {
    require(left >= 0L)
    require(right >= 0L)
    require(common >= 0L)

    if (common == 0L) {
        return 0.0
    }

    if (left == 0L && right == 0L) {
        return 1.0
    }

    require(max(left, right) >= common)

    return (2.0 * common) / (left + right).toDouble()
}

/**
 * @see [calculateDiceValue]
 * */
fun calculateDiceValue(
    common: Int,
    left: Int,
    right: Int
): Double = calculateDiceValue(common.toLong(), left.toLong(), right.toLong())

/**
 * Determine whether the two pairs of GumTree have the same parent.
 * When the first elements of the pairs have the same parent and the second elements of the pairs have the same parent,
 * the function returns true.
 * */
inline fun <reified T : Tree> hasSameParent(
    m1: Pair<T, T>,
    m2: Pair<T, T>
): Boolean = (m1.first.getParent() == m2.first.getParent()) && (m1.second.getParent() == m2.second.getParent())

/**
 * To calculate the number of mapped descendents in [tree1] and [tree2].
 * The descendents of [tree1] should be saved in the Left part of [storage].
 * */
inline fun <reified T : BasicTree<T>> numOfMappedDescendents(
    tree1: T,
    tree2: T,
    storage: TreeMappingStorage<T>
): Int {
    val tree2Descendents = tree2.descendents
    return tree1.descendents.count {
        tree2Descendents.contains(storage.getMappingOfLeft(it))
    }
}