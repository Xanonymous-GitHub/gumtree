package tw.xcc.gumtree.model

class GumTreeView private constructor(target: GumTree) : GumTree(target) {
    private val frozenPreOrdered by lazy { super.preOrdered() }
    private val frozenPostOrdered by lazy { super.postOrdered() }
    private val frozenDescendents by lazy { frozenPreOrdered.drop(1) }
    private val frozenSubTreeSize by lazy { frozenDescendents.size }
    private val frozenAncestors by lazy { super.ancestors.toList() }
    private val frozenHeight by lazy { super.height }
    private val frozenDepth by lazy { super.depth }

    override val descendents: List<GumTree>
        get() = frozenDescendents
    override val ancestors: List<GumTree>
        get() = frozenAncestors
    override val subTreeSize: Int
        get() = frozenSubTreeSize
    override val height: Int
        get() = frozenHeight
    override val depth: Int
        get() = frozenDepth

    override fun postOrdered(): List<GumTree> = frozenPostOrdered

    override fun preOrdered(): List<GumTree> = frozenPreOrdered

    override fun toNewFrozen(): GumTreeView = this

    override fun setChildrenTo(children: List<GumTree>) {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    override fun setParentTo(parent: GumTree?) {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    override fun insertChildAt(
        pos: Int,
        child: GumTree
    ) {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    override fun inOrdered(): List<GumTree> = throw NotImplementedError()

    companion object {
        fun from(gumTree: GumTree): GumTreeView = GumTreeView(gumTree)

        fun frozeEntireTreeFrom(gumTree: GumTree): GumTreeView = gumTree.toNewFrozen()
    }
}