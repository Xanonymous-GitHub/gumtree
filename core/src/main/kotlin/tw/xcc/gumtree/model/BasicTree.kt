package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Tree
import java.util.concurrent.atomic.AtomicReference

/**
 * The general thread-safe implementation of a tree structure.
 * */
abstract class BasicTree<T> : Tree<T> where T : BasicTree<T> {
    protected abstract val self: T

    private val _parent = AtomicReference<T?>()
    final override val parent: T?
        get() = synchronized(this) { _parent.get() }

    protected val childrenMap = AtomicReference(sortedMapOf<Int, T>())
    final override val children: List<T>
        get() = synchronized(this) { childrenMap.get().values.toList() }

    fun addChild(child: T) {
        synchronized(this) {
            val newChildrenMap = childrenMap.get()
            newChildrenMap[newChildrenMap.size] = child.also { it.setParentTo(self) }
            childrenMap.set(newChildrenMap)
        }
    }

    fun setChildrenTo(children: List<T>) =
        with(childrenMap) {
            synchronized(this) {
                val newChildrenMap = sortedMapOf<Int, T>()
                children.forEachIndexed { i, child ->
                    newChildrenMap[i] = child.also { it.setParentTo(self) }
                }
                this.set(newChildrenMap)
            }
        }

    fun setParentTo(parent: T?) {
        synchronized(this) {
            _parent.set(parent)
        }
    }

    final override fun childAt(i: Int): T? = synchronized(this) { childrenMap.get()[i] }

    final override fun childCount(): Int = synchronized(this) { childrenMap.get().size }

    final override fun isRoot(): Boolean = synchronized(this) { parent == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenMap.get().isEmpty() }

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    abstract fun structureHashCode(): Int
}