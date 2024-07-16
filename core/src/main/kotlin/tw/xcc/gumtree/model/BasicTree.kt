package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Tree

internal abstract class BasicTree : Tree {
    private var _parent: BasicTree? = null
    override val parent: BasicTree?
        get() = _parent

    protected val childrenMap = sortedMapOf<Int, BasicTree>()
    override val children: List<BasicTree>
        get() = childrenMap.values.toList()

    fun addChild(child: BasicTree) {
        synchronized(this) {
            childrenMap[childrenMap.size] = child.also { it._parent = this }
        }
    }

    fun setChildren(children: List<BasicTree>) =
        with(childrenMap) {
            synchronized(this) {
                clear()
                children.forEachIndexed { i, child ->
                    this[i] = child.also { it._parent = this@BasicTree }
                }
            }
        }

    fun setParent(parent: BasicTree?) {
        synchronized(this) {
            this._parent = parent
        }
    }

    override fun childAt(i: Int): Tree? = childrenMap[i]

    override fun childCount(): Int = childrenMap.size

    override fun isRoot(): Boolean = _parent == null

    override fun isLeaf(): Boolean = childrenMap.size == 0

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int
}