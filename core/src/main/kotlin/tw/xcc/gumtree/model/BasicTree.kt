package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Traversable
import tw.xcc.gumtree.api.tree.Tree
import tw.xcc.gumtree.helper.postOrderOf
import tw.xcc.gumtree.helper.preOrderOf
import java.io.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * The general thread-safe implementation of a tree structure.
 * */
@OptIn(ExperimentalUuidApi::class)
abstract class BasicTree<T> : Serializable, Tree, Traversable<T> where T : BasicTree<T> {
    protected abstract val self: T

    protected var parentRef: T? = null

    protected val childrenList = mutableListOf<T>()

    protected var idRef = Uuid.random()
    override val id: String
        get() = idRef.toHexString()

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
                    val parent = parentRef
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
                clear()
                addAll(children)
                forEach { it.parentRef = self }
            }
        }

    open fun addChild(child: T) {
        synchronized(this) {
            childrenList.add(
                child.also { it.setParentTo(self) }
            )
        }
    }

    open fun setChildrenTo(children: List<T>) = setChildrenToImpl(children)

    open fun setParentTo(parent: T?) {
        synchronized(this) {
            this.parentRef = parent
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

    final override fun getParent(): T? = synchronized(this) { parentRef }

    final override fun getChildren(): List<T> = synchronized(this) { childrenList }

    final override fun childAt(i: Int): T? = synchronized(this) { childrenList.getOrNull(i) }

    final override fun childCount(): Int = synchronized(this) { childrenList.size }

    final override fun isRoot(): Boolean = synchronized(this) { parentRef == null }

    final override fun isLeaf(): Boolean = synchronized(this) { childrenList.isEmpty() }

    abstract fun similarityProperties(): String

    /**
     * The Equal function ignores the parent and children fields.
     * We only compare the [id] field.
     * */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BasicTree<*>) return false
        if (idRef != other.idRef) return false
        return true
    }

    override fun hashCode(): Int = idRef.hashCode()
}