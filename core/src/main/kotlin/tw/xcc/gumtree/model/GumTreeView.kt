package tw.xcc.gumtree.model

import java.util.concurrent.atomic.AtomicReference

class GumTreeView private constructor(target: GumTree) : GumTree(target.type, target.label, target.pos, target.length) {
    private val frozenPreOrdered by lazy { AtomicReference(target.preOrdered()) }
    private val frozenPostOrdered by lazy { AtomicReference(target.postOrdered()) }
    private val frozenDescendents by lazy { AtomicReference(frozenPreOrdered.get().drop(1)) }
    private val frozenSubTreeSize by lazy { AtomicReference(frozenDescendents.get().size) }
    private val frozenAncestors by lazy { AtomicReference(target.ancestors.toList()) }
    private val frozenHeight by lazy { AtomicReference(target.height) }
    private val frozenDepth by lazy { AtomicReference(target.depth) }

    override val id: String = target.id
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

    override fun toFrozen(): GumTreeView = this

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

        fun frozeEntireTreeFrom(gumTree: GumTree): GumTreeView = gumTree.toFrozen()
    }
}