package tw.xcc.gumtree.api.tree

import org.antlr.v4.runtime.tree.Tree as AntlrTree

/**
 * Tree is the top-and-only adaptor interface for ANTLR's [Tree] interface.
 * */
internal interface Tree : AntlrTree {
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
    override fun getChildCount(): Int

    /**
     * To get the child at the specified index.
     * */
    override fun getChild(i: Int): Tree

    /**
     * To get the parent node.
     * */
    override fun getParent(): Tree?

    /**
     * To get the serialized tree as a string.
     * */
    override fun toStringTree(): String

    /**
     * To get the payload of the current node.
     * */
    override fun getPayload(): Any
}