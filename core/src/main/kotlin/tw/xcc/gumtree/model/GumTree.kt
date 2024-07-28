package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.helper.isIsoStructuralTo
import tw.xcc.gumtree.helper.isIsomorphicTo
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

open class GumTree(
    type: TreeType,
    /**
     * The Label corresponds to the actual tokens in the code.
     * */
    var label: String = EMPTY_LABEL,
    /**
     * pos is the position of the node in the source code.
     * */
    val pos: Int = -1,
    /**
     * length is the length of the node in the source code.
     * for example, the length of a token is the length of the token itself.
     * */
    val length: Int = -1
) : BasicTree<GumTree>(), Comparable<GumTree> {
    constructor(other: GumTree) : this(other.type, other.label, other.pos, other.length) {
        val otherChildren = other.childrenMap.get()
        otherChildren.forEach { (pos, child) ->
            insertChildAtImpl(pos, GumTree(child))
        }
        idRef.set(other.idRef.get())
        parent.set(other.parent.get())
    }

    private val _type = AtomicReference(TreeType.empty())

    /**
     * The Type corresponds to the name of their production rule in the grammar.
     * */
    var type: TreeType
        get() = _type.get()
        set(value) = _type.set(value)

    private val _positionOfParent = AtomicInteger(-1)
    val positionOfParent: Int
        get() = _positionOfParent.get()

    init {
        _type.set(type)
    }

    private fun insertChildAtImpl(
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
            newChildrenMap[pos] =
                child.also {
                    it.setParentTo(this)
                    it._positionOfParent.set(pos)
                }
            childrenMap.set(newChildrenMap)
        }
    }

    open fun insertChildAt(
        pos: Int,
        child: GumTree
    ) = insertChildAtImpl(pos, child)

    open fun toNewFrozen(): GumTreeView =
        synchronized(this) {
            val children = childrenMap.get()
            children.replaceAll { _, v -> v.toNewFrozen() }
            childrenMap.set(children)
            return GumTreeView.from(this)
        }

    infix fun hasSameTypeAs(other: GumTree): Boolean = type == other.type

    infix fun hasSameLabelAs(other: GumTree): Boolean = label == other.label

    final override infix fun isIsomorphicTo(other: GumTree): Boolean = isIsomorphicTo(this, other)

    final override infix fun isIsoStructuralTo(other: GumTree): Boolean = isIsoStructuralTo(this, other)

    final override fun similarityProperties(): String = "<$label>[$type]<"

    final override val self: GumTree
        get() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GumTree) return false
        if (label != other.label) return false
        if (pos != other.pos) return false
        if (length != other.length) return false
        if (type != other.type) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        val hashCodes =
            arrayOf(
                label.hashCode(),
                pos.hashCode(),
                length.hashCode(),
                type.hashCode(),
                super.hashCode()
            )
        return hashCodes.fold(0) { acc, i -> 31 * acc + i }
    }

    companion object {
        private const val EMPTY_LABEL = ""
    }
}