package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.helper.isIsoStructuralTo
import tw.xcc.gumtree.helper.isIsomorphicTo

open class GumTree(
    /**
     * [info] contains the information of the source code token represented by the tree node.
     * */
    open var info: Info = Info.empty()
) : BasicTree<GumTree>(), Comparable<GumTree> {
    constructor(other: GumTree) : this(other.info) {
        val otherChildren = other.childrenList.get()
        this.setChildrenToImpl(otherChildren.map { GumTree(it) })
        idRef.set(other.idRef.get())
        parent.set(other.parent.get())
    }

    open val positionOfParent: Int
        get() = calculatePosOfParent()

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

    infix fun hasSameTypeAs(other: GumTree): Boolean = info.type == other.info.type

    infix fun hasSameTextAs(other: GumTree): Boolean = info.text == other.info.text

    final override suspend infix fun isIsomorphicTo(other: GumTree): Boolean = isIsomorphicTo(this, other)

    final override suspend infix fun isIsoStructuralTo(other: GumTree): Boolean = isIsoStructuralTo(this, other)

    final override fun similarityProperties(): String = "<${info.text}>[${info.type}]<"

    final override val self: GumTree
        get() = this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GumTree) return false
        if (info != other.info) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        val hashCodes =
            arrayOf(
                info.hashCode(),
                super.hashCode()
            )
        return hashCodes.fold(0) { acc, i -> 31 * acc + i }
    }

    override fun toString(): String = "GumTree($info, ${id.subSequence(0, 8)})"

    /**
     * Info is a data class that contains the information of a tree node.
     * */
    data class Info(
        /**
         * See [TreeType].
         * */
        val type: TreeType,
        /**
         * [text] corresponds to the actual tokens in the code.
         * */
        val text: String = EMPTY_LABEL,
        /**
         * [line] corresponds to the line number of the token in the source code file.
         * */
        val line: Int,
        /**
         * [posOfLine] corresponds to the first char start position of the token in the line.
         * */
        val posOfLine: Int
    ) {
        companion object {
            private const val EMPTY_LABEL = ""

            fun of(
                type: TreeType = TreeType.empty(),
                text: String = EMPTY_LABEL,
                line: Int = -1,
                posOfLine: Int = -1
            ) = Info(type, text, line, posOfLine)

            fun empty() = of()
        }
    }
}