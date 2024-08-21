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

    /**
     * Traverse the tree in in-order. This means that the current node is visited between its children.
     * Note that not every tree has a meaningful in-order traversal,
     * since it requires how we define the left and right children.
     * For example, a binary tree has a meaningful in-order traversal, but a general tree does not.
     * So the default implementation is to return an empty list.
     * */
    // fun inOrdered(): List<E> = emptyList()
}