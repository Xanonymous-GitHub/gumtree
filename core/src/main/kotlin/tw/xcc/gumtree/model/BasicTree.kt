package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Traversable
import tw.xcc.gumtree.api.tree.Tree
import tw.xcc.gumtree.helper.postOrdered
import tw.xcc.gumtree.helper.preOrdered
import java.util.concurrent.atomic.AtomicReference

/**
 * The general thread-safe implementation of a tree structure.
 * */
abstract class BasicTree<T> : Tree, Traversable<T> where T : BasicTree<T> {
    protected abstract val self: T

    private val parent = AtomicReference<T?>()

    protected val childrenMap = AtomicReference(sortedMapOf<Int, T>())

    private val _metrics = AtomicReference(TreeMetrics.empty())
    val metrics: TreeMetrics
        get() = _metrics.get()

    val descendents: List<T> by lazy {
        synchronized(this) {
            preOrdered().drop(1)
        }
    }

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
            this.parent.set(parent)
        }
    }

    final override fun preOrdered(): List<T> {
        val result = mutableListOf<T>()
        preOrdered(self) { result.add(it) }
        return result
    }

    final override fun postOrdered(): List<T> {
        val result = mutableListOf<T>()
        postOrdered(self) { result.add(it) }
        return result
    }

    final override fun getParent(): T? = synchronized(this) { parent.get() }

    final override fun getChildren(): List<T> = synchronized(this) { childrenMap.get().values.toList() }

    final override fun childAt(i: Int): T? = synchronized(this) { childrenMap.get()[i] }

    final override fun childCount(): Int = synchronized(this) { childrenMap.get().size }

    final override fun isRoot(): Boolean = synchronized(this) { parent.get() == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenMap.get().isEmpty() }

    abstract fun similarityHashCode(): Int

    abstract fun similarityStructureHashCode(): Int
}