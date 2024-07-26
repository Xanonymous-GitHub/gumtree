package tw.xcc.gumtree.algorithms

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
private inline fun <reified T> calculateLCSLength(
    collection1: Collection<T>,
    collection2: Collection<T>,
    crossinline equals: (T, T) -> Boolean
): Array<IntArray> {
    contract {
        callsInPlace(equals, InvocationKind.UNKNOWN)
    }

    if (collection1.isEmpty() || collection2.isEmpty()) {
        return emptyArray()
    }

    val lengths = Array(collection1.size + 1) { IntArray(collection2.size + 1) }

    for (i in collection1.indices) {
        for (j in collection2.indices) {
            if (equals(collection1.elementAt(i), collection2.elementAt(j))) {
                lengths[i + 1][j + 1] = lengths[i][j] + 1
            } else {
                lengths[i + 1][j + 1] = maxOf(lengths[i + 1][j], lengths[i][j + 1])
            }
        }
    }

    return lengths
}

private fun <T> findCommonElements(
    collection1: Collection<T>,
    collection2: Collection<T>,
    calculatedLengths: Array<IntArray>
): List<Pair<T, T>> {
    val lcs = mutableListOf<Pair<T, T>>()

    if (calculatedLengths.isEmpty()) {
        return lcs
    }

    var i = collection1.size
    var j = collection2.size
    while (i != 0 && j != 0) {
        if (calculatedLengths[i][j] == calculatedLengths[i - 1][j]) {
            i--
        } else if (calculatedLengths[i][j] == calculatedLengths[i][j - 1]) {
            j--
        } else {
            lcs.add(collection1.elementAt(i - 1) to collection2.elementAt(j - 1))
            i--
            j--
        }
    }

    return lcs.reversed()
}

/**
 * Longest Common Subsequence (LCS) algorithm.
 * This function only calculates the size of the LCS.
 * */
internal inline fun <reified T> lcsBaseOnlySize(
    collection1: Collection<T>,
    collection2: Collection<T>,
    crossinline equals: (T, T) -> Boolean = { a, b -> a == b }
): Int {
    val calculatedLengths = calculateLCSLength(collection1, collection2, equals)
    if (calculatedLengths.isEmpty()) {
        return 0
    }

    return calculatedLengths.last().last()
}

/**
 * Longest Common Subsequence (LCS) algorithm.
 * This function calculates the LCS and returns the common elements.
 * */
internal inline fun <reified T> lcsBaseWithElements(
    collection1: Collection<T>,
    collection2: Collection<T>,
    crossinline equals: (T, T) -> Boolean = { a, b -> a == b }
): List<Pair<T, T>> {
    val calculatedLengths = calculateLCSLength(collection1, collection2, equals)
    return findCommonElements(collection1, collection2, calculatedLengths)
}