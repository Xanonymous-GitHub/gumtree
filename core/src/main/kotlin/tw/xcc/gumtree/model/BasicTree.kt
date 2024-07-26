package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Traversable
import tw.xcc.gumtree.api.tree.Tree
import tw.xcc.gumtree.helper.postOrderOf
import tw.xcc.gumtree.helper.preOrderOf
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * The general thread-safe implementation of a tree structure.
 * */
abstract class BasicTree<T> : Tree, Traversable<T> where T : BasicTree<T> {
    protected abstract val self: T

    private val parent = AtomicReference<T?>()

    protected val childrenMap = AtomicReference(sortedMapOf<Int, T>())

    override val id: String = UUID.randomUUID().toString()

    private val _height = AtomicInteger(0)
    open val height: Int by lazy { calculateHeight() }

    private val _depth = AtomicInteger(0)
    open val depth: Int by lazy { calculateDepth() }

    open val descendents: List<T> by lazy {
        synchronized(this) {
            preOrdered().drop(1)
        }
    }

    /**
     * The sequence of the node's ancestor nodes.
     * Sorted by their distance to the node.
     * */
    open val ancestors: List<T> by lazy {
        synchronized(this) {
            if (isRoot()) {
                emptyList()
            } else {
                val parent = parent.get()
                assert(parent != null)
                listOf(parent!!) + parent.ancestors
            }
        }
    }

    open val subTreeSize: Int by lazy { descendents.size }

    private fun calculateHeight(): Int =
        synchronized(this) {
            return if (childCount() == 0) {
                0
            } else {
                getChildren().maxOf { it.height } + 1
            }
        }

    private fun calculateDepth(): Int =
        synchronized(this) {
            val parent = getParent()
            return if (parent == null) {
                0
            } else {
                parent.depth + 1
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
        preOrderOf(self) { result.add(it) }
        return result
    }

    final override fun postOrdered(): List<T> {
        val result = mutableListOf<T>()
        postOrderOf(self) { result.add(it) }
        return result
    }

    final override fun getParent(): T? = synchronized(this) { parent.get() }

    final override fun getChildren(): List<T> = synchronized(this) { childrenMap.get().values.toList() }

    final override fun childAt(i: Int): T? = synchronized(this) { childrenMap.get()[i] }

    final override fun childCount(): Int = synchronized(this) { childrenMap.get().size }

    final override fun isRoot(): Boolean = synchronized(this) { parent.get() == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenMap.get().isEmpty() }

    abstract fun similarityProperties(): String
}