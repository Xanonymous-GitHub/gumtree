package tw.xcc.gumtree.api.tree

internal interface TreeMatcher<T : Tree> {
    /**
     * Match two trees and save their results into a [TreeMappingStorage].
     * */
    fun match(
        tree1: T,
        tree2: T
    ): TreeMappingStorage<T>

    /**
     * Match two trees and save their results into the provided [TreeMappingStorage].
     * */
    fun match(
        tree1: T,
        tree2: T,
        storage: TreeMappingStorage<T>
    ): TreeMappingStorage<T>
}