package tw.xcc.gumtree.model

import java.util.concurrent.atomic.AtomicReference

class GumTreeView private constructor(target: GumTree) : GumTree(target.type, target.label) {
    private val frozenDescendents = AtomicReference(target.descendents.toList())
    private val frozenAncestors = AtomicReference(target.ancestors.toList())
    private val frozenHeight = AtomicReference(target.height)
    private val frozenDepth = AtomicReference(target.depth)
    private val frozenSubTreeSize = AtomicReference(frozenDescendents.get().size)

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

    override fun toFrozen(): GumTreeView = this

    companion object {
        fun from(gumTree: GumTree): GumTreeView = GumTreeView(gumTree)

        fun frozeEntireTreeFrom(gumTree: GumTree): GumTreeView = gumTree.toFrozen()
    }
}