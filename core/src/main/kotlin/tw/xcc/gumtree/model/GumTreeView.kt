package tw.xcc.gumtree.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GumTreeView private constructor(target: GumTree) : GumTree(target.info.copy()) {
    init {
        super.idRef = Uuid.parseHex(target.id)
    }

    private val frozenPreOrdered by lazy { super.preOrdered() }
    private val frozenPostOrdered by lazy { super.postOrdered() }
    private val frozenDescendents by lazy { frozenPreOrdered.drop(1) }
    private val frozenSubTreeSize by lazy { frozenDescendents.size }
    private val frozenAncestors by lazy { super.ancestors.toList() }
    private val frozenHeight by lazy { super.height }
    private val frozenDepth by lazy { super.depth }
    private val frozenPosOfParent by lazy { super.positionOfParent }
    private val frozenInfo by lazy { super.info }

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
    override val positionOfParent: Int
        get() = frozenPosOfParent
    override var info: Info = frozenInfo
        set(_) = throw NoSuchMethodException("calling this method in GumTreeView is not allowed")

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

    override fun addChild(child: GumTree) {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    override fun tryRemoveChild(child: GumTree): Boolean {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    override fun leaveParent(): Boolean {
        throw NoSuchMethodException("calling this method in GumTreeView is not allowed")
    }

    companion object {
        fun from(gumTree: GumTree): GumTreeView = GumTreeView(gumTree)

        /**
         * Convert the whole tree of the subtree [gumTree] to a new frozen tree.
         * Note that the returned tree is the root of the entire tree, not the [gumTree]'s view.
         * */
        fun frozeEntireTreeFrom(gumTree: GumTree): GumTreeView {
            val root = gumTree.ancestors.lastOrNull() ?: gumTree.toNewFrozen()
            return root.toNewFrozen()
        }
    }
}