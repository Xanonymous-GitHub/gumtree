package tw.xcc.gumtree.api.tree

/**
 * Common interface for defining a tree structure.
 * A tree is a hierarchical structure that consists of nodes connected by edges.
 * The number of children can be any non-negative integer.
 * */
interface Tree {
    /**
     * The unique identifier of the Tree.
     * */
    val id: String

    /**
     * To check if the current node is a root node.
     * */
    fun isRoot(): Boolean

    /**
     * To check if the current node is a leaf node.
     * */
    fun isLeaf(): Boolean

    /**
     * To get the number of children.
     * */
    fun childCount(): Int

    /**
     * To get the child at the specified index.
     * */
    fun childAt(i: Int): Tree?

    /**
     * To get the children of the Tree.
     * */
    fun getChildren(): List<Tree>

    /**
     * To get the parent of the Tree.
     * */
    fun getParent(): Tree?
}