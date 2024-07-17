package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable
import tw.xcc.gumtree.api.tree.Traversable

class GumTree : BasicTree<GumTree>(), Traversable<GumTree>, Comparable<GumTree> {
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

    private fun preOrderedImpl(
        tree: GumTree,
        visited: MutableSet<GumTree>
    ) {
        synchronized(tree) {
            visited.add(tree)
            tree.children.forEach {
                preOrderedImpl(it, visited)
            }
        }
    }

    override fun preOrdered(): List<GumTree> {
        val visited = mutableSetOf<GumTree>()
        preOrderedImpl(this, visited)
        return visited.toList()
    }

    private fun postOrderedImpl(
        tree: GumTree,
        visited: MutableSet<GumTree>
    ) {
        synchronized(tree) {
            tree.children.forEach {
                postOrderedImpl(it, visited)
            }
            visited.add(tree)
        }
    }

    override fun postOrdered(): List<GumTree> {
        val visited = mutableSetOf<GumTree>()
        postOrderedImpl(this, visited)
        return visited.toList()
    }

    override infix fun isIsomorphicTo(other: GumTree): Boolean {
        TODO("Not yet implemented")
    }

    override infix fun isIsoStructuralTo(other: GumTree): Boolean {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hashCode(): Int {
        TODO("Not yet implemented")
    }

    override fun structureHashCode(): Int {
        TODO("Not yet implemented")
    }

    override val self: GumTree
        get() = this
}