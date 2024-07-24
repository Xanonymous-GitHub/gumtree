package tw.xcc.gumtree.helper

infix fun <E1, E2> Collection<E1>.crossProductOf(other: Collection<E2>): List<Pair<E1, E2>> {
    val result = mutableListOf<Pair<E1, E2>>()
    for (e1 in this) {
        for (e2 in other) {
            result.add(e1 to e2)
        }
    }
    return result
}