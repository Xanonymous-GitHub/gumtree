package tw.xcc.gumtree.api.tree

import tw.xcc.gumtree.model.BasicTree

internal interface PriorityTreeList<T : BasicTree<T>> {
    /**
     * Inserts the node n in the list.
     * */
    fun push(tree: T)

    /**
     * Inserts all the children of n in the list.
     * */
    fun open(tree: T)

    /**
     * Returns and removes the set of all nodes which has a priority equals to [peekMax]
     * */
    fun pop(): List<T>

    /**
     * Returns and removes the set of all nodes which has a priority equals to [peekMax],
     * and inserts all the children of these nodes in the list.
     * */
    fun popOpen(): List<T>

    /**
     * Returns the highest priority value of the list.
     * */
    fun peekMax(): Int

    /**
     * Returns whether the list is empty or not.
     * */
    fun isEmpty(): Boolean

    /**
     * Clears the list.
     * */
    fun clear()
}