package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import java.util.concurrent.atomic.AtomicReference

class GumTree(
    type: TreeType,
    val label: String = EMPTY_LABEL
) : BasicTree<GumTree>(),
    Comparable<GumTree> {
    private val compareHelper = CompareHelper(this)

    var pos: Int = -1
    var length: Int = -1

    private val _type = AtomicReference(TreeType.empty())
    val type: TreeType
        get() = _type.get()

    private val _metrics = AtomicReference(TreeMetrics.empty()) // TODO: calculate metrics
    val metrics: TreeMetrics
        get() = _metrics.get()

    fun setTypeTo(value: TreeType) =
        synchronized(this) {
            _type.set(value)
        }

    fun setMetricsTo(value: TreeMetrics) =
        synchronized(this) {
            _metrics.set(value)
        }

    fun insertChildAt(
        pos: Int,
        child: GumTree
    ) {
        synchronized(this) {
            if (pos < 0) {
                throw IndexOutOfBoundsException("The position $pos should be non-negative.")
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

    override infix fun isIsomorphicTo(other: GumTree): Boolean = compareHelper.isIsomorphicTo(other)

    override infix fun isIsoStructuralTo(other: GumTree): Boolean = compareHelper.isIsoStructuralTo(other)

    override fun similarityHashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun similarityStructureHashCode(): Int {
        TODO("Not yet implemented")
    }

    override val traversalHelper = TraversalHelper(this)

    override val self: GumTree
        get() = this

    companion object {
        const val EMPTY_LABEL = ""
    }

    init {
        setTypeTo(type)
    }
}