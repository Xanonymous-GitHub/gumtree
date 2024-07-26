package tw.xcc.gumtree.api.tree

interface TreeMappingStorage<T : Tree> {
    fun addMappingOf(mapping: Pair<T, T>)

    fun addMappingRecursivelyOf(mapping: Pair<T, T>)

    fun removeMappingOf(mapping: Pair<T, T>)

    fun getMappingOfLeft(left: T): T?

    fun getMappingOfRight(right: T): T?

    fun isLeftMapped(left: T): Boolean

    fun isAnyOfLeftsUnMapped(lefts: Iterable<T>): Boolean

    fun isRightMapped(right: T): Boolean

    fun isAnyOfRightsUnMapped(rights: Iterable<T>): Boolean

    fun areBothUnMapped(mapping: Pair<T, T>): Boolean

    fun hasUnMappedDescendentOfLeft(left: T): Boolean

    fun hasUnMappedDescendentOfRight(right: T): Boolean
}