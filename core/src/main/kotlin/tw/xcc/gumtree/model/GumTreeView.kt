package tw.xcc.gumtree.model

import java.util.concurrent.atomic.AtomicReference

class GumTreeView private constructor(target: GumTree) : GumTree(target) {
    private val frozenPreOrdered by lazy { AtomicReference(super.preOrdered()) }
    private val frozenPostOrdered by lazy { AtomicReference(super.postOrdered()) }
    private val frozenDescendents by lazy { AtomicReference(frozenPreOrdered.get().drop(1)) }
    private val frozenSubTreeSize by lazy { AtomicReference(frozenDescendents.get().size) }
    private val frozenAncestors by lazy { AtomicReference(super.ancestors.toList()) }
    private val frozenHeight by lazy { AtomicReference(super.height) }
    private val frozenDepth by lazy { AtomicReference(super.depth) }

    override val descendents: List<GumTree>
        get() = frozenDescendents.get()
    override val ancestors: List<GumTree>
        get() = frozenAncestors.get()
    override val subTreeSize: Int
        get() = frozenSubTreeSize.get()
    override val height: Int
        get() = frozenHeight.get()
    override val depth: Int
        get() = frozenDepth.get()

    override fun postOrdered(): List<GumTree> = frozenPostOrdered.get()

    override fun preOrdered(): List<GumTree> = frozenPreOrdered.get()

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