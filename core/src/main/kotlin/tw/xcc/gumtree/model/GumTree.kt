package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.helper.isIsoStructuralTo
import tw.xcc.gumtree.helper.isIsomorphicTo
import java.util.concurrent.atomic.AtomicReference

class GumTree(
    type: TreeType,
    var label: String = EMPTY_LABEL
) : BasicTree<GumTree>(), Comparable<GumTree> {
    var pos: Int = -1
    var length: Int = -1

    private val _type = AtomicReference(TreeType.empty())
    val type: TreeType
        get() = _type.get()

    init {
        _type.set(type)
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

    override infix fun isIsomorphicTo(other: GumTree): Boolean = isIsomorphicTo(this, other)

    override infix fun isIsoStructuralTo(other: GumTree): Boolean = isIsoStructuralTo(this, other)

    override fun similarityHashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun similarityStructureHashCode(): Int {
        TODO("Not yet implemented")
    }

    override val self: GumTree
        get() = this

    companion object {
        const val EMPTY_LABEL = ""
    }
}