package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Traversable
import tw.xcc.gumtree.api.tree.Tree
import tw.xcc.gumtree.helper.postOrderOf
import tw.xcc.gumtree.helper.preOrderOf
import java.io.Serializable
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

/**
 * The general thread-safe implementation of a tree structure.
 * */
abstract class BasicTree<T> : Serializable, Tree, Traversable<T> where T : BasicTree<T> {
    protected abstract val self: T

    protected val parent = AtomicReference<T?>()

    protected val childrenList = AtomicReference(mutableListOf<T>())

    protected val idRef = AtomicReference(UUID.randomUUID().toString())
    override val id: String
        get() = idRef.get()

    open val height: Int
        get() = calculateHeight()

    open val depth: Int
        get() = calculateDepth()

    open val descendents: List<T>
        get() = preOrdered().drop(1)

    /**
     * The sequence of the node's ancestor nodes.
     * Sorted by their distance to the node.
     * */
    open val ancestors: List<T>
        get() =
            synchronized(this) {
                if (isRoot()) {
                    emptyList()
                } else {
                    val parent = parent.get()
                    assert(parent != null)
                    listOf(parent!!) + parent.ancestors
                }
            }

    open val subTreeSize: Int
        get() = descendents.size

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

    protected fun setChildrenToImpl(children: List<T>) =
        with(childrenList) {
            synchronized(this) {
                val newChildrenList = mutableListOf<T>()
                newChildrenList.addAll(children)
                newChildrenList.forEach { it.parent.set(self) }
                this.set(newChildrenList)
            }
        }

    open fun addChild(child: T) {
        synchronized(this) {
            val newChildrenList = childrenList.get()
            newChildrenList.add(
                child.also { it.setParentTo(self) }
            )
            childrenList.set(newChildrenList)
        }
    }

    open fun setChildrenTo(children: List<T>) = setChildrenToImpl(children)

    open fun setParentTo(parent: T?) {
        synchronized(this) {
            this.parent.set(parent)
        }
    }

    override fun preOrdered(): List<T> {
        val result = mutableListOf<T>()
        preOrderOf(self) { result.add(it) }
        return result
    }

    override fun postOrdered(): List<T> {
        val result = mutableListOf<T>()
        postOrderOf(self) { result.add(it) }
        return result
    }

    final override fun getParent(): T? = synchronized(this) { parent.get() }

    final override fun getChildren(): List<T> = synchronized(this) { childrenList.get() }

    final override fun childAt(i: Int): T? = synchronized(this) { childrenList.get().getOrNull(i) }

    final override fun childCount(): Int = synchronized(this) { childrenList.get().size }

    final override fun isRoot(): Boolean = synchronized(this) { parent.get() == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenList.get().isEmpty() }

    abstract fun similarityProperties(): String

    /**
     * The Equal function ignores the parent and children fields.
     * We only compare the [id] field.
     * */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BasicTree<*>) return false
        if (idRef.get() != other.idRef.get()) return false
        return true
    }

    override fun hashCode(): Int = idRef.get().hashCode()
}