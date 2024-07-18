package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.api.tree.Traversable

class GumTree :
    BasicTree<GumTree>(),
    Traversable<GumTree>,
    Comparable<GumTree> {
    private val traversalHelper = TraversalHelper(this)
    private val compareHelper = CompareHelper(this)

    val pos: Int = -1
    val length: Int = -1
    private var label: String = ""

    private var _type = TreeType.empty()
    private var type: TreeType
        get() = _type
        set(value) =
            synchronized(this) {
                _type = value
            }

    private var _metrics = TreeMetrics.empty()
    var metrics: TreeMetrics
        get() = _metrics
        set(value) =
            synchronized(this) {
                _metrics = value
            }

    val descendents: List<GumTree> by lazy {
        synchronized(this) {
            preOrdered().drop(1)
        }
    }

    fun insertChildAt(
        pos: Int,
        child: GumTree
    ) {
        synchronized(this) {
            val childCount = childCount()
            if (pos < 0 || pos > childCount) {
                throw IndexOutOfBoundsException("Index: $pos, Size: $childCount")
            }

            if (childrenMap.get().containsKey(pos)) {
                throw IllegalArgumentException("The position $pos is already occupied.")
            }

            val newChildrenMap = childrenMap.get()
            newChildrenMap[pos] = child.also { it.setParentTo(this) }
            childrenMap.set(newChildrenMap)
        }
    }

    infix fun hasSameTypeAs(other: GumTree): Boolean = type == other.type

    infix fun hasSameLabelAs(other: GumTree): Boolean = label == other.label

    override fun preOrdered(): List<GumTree> = traversalHelper.preOrdered()

    override fun postOrdered(): List<GumTree> = traversalHelper.postOrdered()

    override infix fun isIsomorphicTo(other: GumTree): Boolean = compareHelper.isIsomorphicTo(other)

    override infix fun isIsoStructuralTo(other: GumTree): Boolean = compareHelper.isIsoStructuralTo(other)

    override fun similarityHashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun similarityStructureHashCode(): Int {
        TODO("Not yet implemented")
    }

    override val self: GumTree
        get() = this
}