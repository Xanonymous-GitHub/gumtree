package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Traversable
import tw.xcc.gumtree.api.tree.Tree

class TraversalHelper<T>(
    private val tree: T
) : Traversable<T> where T : Tree<T>, T : Any {
    private fun preOrderedImpl(
        tree: T,
        visited: MutableSet<T>
    ) {
        visited.add(tree)
        tree.children.forEach {
            preOrderedImpl(it, visited)
        }
    }

    private fun postOrderedImpl(
        tree: T,
        visited: MutableSet<T>
    ) {
        tree.children.forEach {
            postOrderedImpl(it, visited)
        }
        visited.add(tree)
    }

    override fun preOrdered(): List<T> {
        synchronized(tree) {
            val visited = mutableSetOf<T>()
            preOrderedImpl(tree, visited)
            return visited.toList()
        }
    }

    override fun postOrdered(): List<T> {
        synchronized(tree) {
            val visited = mutableSetOf<T>()
            postOrderedImpl(tree, visited)
            return visited.toList()
        }
    }
}