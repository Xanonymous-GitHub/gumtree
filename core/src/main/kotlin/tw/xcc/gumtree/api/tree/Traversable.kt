package tw.xcc.gumtree.api.tree

/**
 * Traversable defines the basic methods for traversing a [Tree].
 * */
internal interface Traversable<out E> where E : Tree, E : Any {
    /**
     * Traverse the tree in pre-order. This means that the current node is visited before its children.
     * */
    fun preOrdered(): List<E>

    /**
     * Traverse the tree in post-order. This means that the current node is visited after its children.
     * */
    fun postOrdered(): List<E>
}