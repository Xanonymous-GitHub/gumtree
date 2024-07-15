package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Tree

internal abstract class BasicTree : Tree, Any() {
    private var parent: BasicTree? = null

    private val _children = mutableListOf<BasicTree>()
    val children: List<BasicTree>
        get() = _children

    fun addChild(child: BasicTree) {
        _children.add(child.also { it.parent = this })
    }

    fun setChildren(children: List<BasicTree>) =
        with(_children) {
            clear()
            addAll(children)
            forEach { it.parent = this@BasicTree }
        }

    fun setParent(parent: BasicTree?) {
        this.parent = parent
    }

    override fun getChild(i: Int): BasicTree = _children[i]

    override fun getChildCount(): Int = _children.size

    override fun getParent(): BasicTree? = parent

    override fun getPayload(): Any = TODO("Not yet implemented")

    override fun toStringTree(): String = TODO("Not yet implemented")

    override fun isRoot(): Boolean = parent == null

    override fun isLeaf(): Boolean = childCount == 0

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int
}