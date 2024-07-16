package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Tree
import java.util.concurrent.atomic.AtomicReference

/**
 * The general thread-safe implementation of a tree structure.
 * */
internal abstract class BasicTree : Tree {
    private val _parent = AtomicReference<BasicTree?>()
    final override var parent: BasicTree?
        get() = synchronized(this) { _parent.get() }
        private set(value) {
            synchronized(this) {
                _parent.set(value)
            }
        }

    protected val childrenMap = AtomicReference(sortedMapOf<Int, BasicTree>())
    final override val children: List<BasicTree>
        get() = synchronized(this) { childrenMap.get().values.toList() }

    fun addChild(child: BasicTree) {
        synchronized(this) {
            val newChildrenMap = childrenMap.get()
            newChildrenMap[newChildrenMap.size] = child.also { it.parent = this }
            childrenMap.set(newChildrenMap)
        }
    }

    fun setChildren(children: List<BasicTree>) =
        with(childrenMap) {
            synchronized(this) {
                val newChildrenMap = sortedMapOf<Int, BasicTree>()
                children.forEachIndexed { i, child ->
                    newChildrenMap[i] = child.also { it.parent = this@BasicTree }
                }
                this.set(newChildrenMap)
            }
        }

    fun setParent(parent: BasicTree?) {
        synchronized(this) {
            this.parent = parent
        }
    }

    final override fun childAt(i: Int): Tree? = synchronized(this) { childrenMap.get()[i] }

    final override fun childCount(): Int = synchronized(this) { childrenMap.get().size }

    final override fun isRoot(): Boolean = synchronized(this) { parent == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenMap.get().isEmpty() }

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int
}