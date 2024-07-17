package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.api.tree.Traversable

class GumTree :
    BasicTree<GumTree>(),
    Traversable<GumTree>,
    Comparable<GumTree> {
    private val traversalHelper = TraversalHelper(this)
    private val compareHelper = CompareHelper(this)

    
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