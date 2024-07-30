package tw.xcc.gumtree.api.tree

/**
 * Comparable defines the basic methods for comparing two [Tree]s.
 * */
internal interface Comparable<T> where T : Tree, T : Any {
    /**
     * To compare whether this tree is strictly equal to another tree.
     * All the node's properties must be equal.
     * */
    suspend infix fun isIsomorphicTo(other: T): Boolean

    /**
     * To compare whether this tree is structurally equal to another tree.
     * Only requires the tree's structure to be equal, not all the node's properties.
     * */
    suspend infix fun isIsoStructuralTo(other: T): Boolean
}