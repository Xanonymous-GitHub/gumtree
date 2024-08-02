package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.helper.isIsoStructuralTo
import tw.xcc.gumtree.helper.isIsomorphicTo
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
        val otherChildren = other.childrenList.get()
        this.setChildrenToImpl(otherChildren.map { GumTree(it) })
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

    open val positionOfParent: Int
        get() = calculatePosOfParent()

    init {
        _type.set(type)
    }

    private fun insertChildAtImpl(
        pos: Int,
        child: GumTree
    ) {
        synchronized(this) {
            val newChildrenList = childrenList.get()

            newChildrenList.add(
                pos,
                child.also { it.setParentTo(this) }
            )
            childrenList.set(newChildrenList)
        }
    }

    private fun calculatePosOfParent(): Int =
        synchronized(this) {
            val theParent = parent.get() ?: return -1
            val children = theParent.childrenList.get()
            return children.lastIndexOf(self)
        }

    open fun insertChildAt(
        pos: Int,
        child: GumTree
    ) = insertChildAtImpl(pos, child)

    open fun tryRemoveChild(child: GumTree): Boolean =
        synchronized(this) {
            val pos = child.positionOfParent
            val children = childrenList.get()

            if (pos !in children.indices) {
                return false
            }

            val removedChild = children.removeAt(pos)

            removedChild.setParentTo(null)
            childrenList.set(children)

            return true
        }

    open fun leaveParent(): Boolean =
        synchronized(this) {
            val theParent = parent.get() ?: return false
            return theParent.tryRemoveChild(self)
        }

    open fun toNewFrozen(): GumTreeView =
        synchronized(this) {
            val children = childrenList.get()
            children.replaceAll { it.toNewFrozen() }
            childrenList.set(children)
            return GumTreeView.from(this)
        }

    infix fun hasSameTypeAs(other: GumTree): Boolean = type == other.type

    infix fun hasSameLabelAs(other: GumTree): Boolean = label == other.label

    final override suspend infix fun isIsomorphicTo(other: GumTree): Boolean = isIsomorphicTo(this, other)

    final override suspend infix fun isIsoStructuralTo(other: GumTree): Boolean = isIsoStructuralTo(this, other)

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

    override fun toString(): String = "GumTree($type, $label, pos=$pos, length=$length, ${id.subSequence(0, 8)})"

    companion object {
        private const val EMPTY_LABEL = ""
    }
}