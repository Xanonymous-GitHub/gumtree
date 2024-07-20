package tw.xcc.gumtree.helper

import tw.xcc.gumtree.api.tree.Tree

private fun <T : Tree> preOrderedImpl(
    tree: T,
    visited: MutableList<T>
) {
    visited.add(tree)
    tree.getChildren().forEach {
        @Suppress("UNCHECKED_CAST")
        preOrderedImpl(it as T, visited)
    }
}

private fun <T : Tree> postOrderedImpl(
    tree: T,
    visited: MutableList<T>
) {
    tree.getChildren().forEach {
        @Suppress("UNCHECKED_CAST")
        postOrderedImpl(it as T, visited)
    }
    visited.add(tree)
}

fun <T : Tree> preOrdered(tree: T): List<T> {
    synchronized(tree) {
        val visited = mutableListOf<T>()
        preOrderedImpl(tree, visited)
        return visited.toList()
    }
}

fun <T : Tree> postOrdered(tree: T): List<T> {
    synchronized(tree) {
        val visited = mutableListOf<T>()
        postOrderedImpl(tree, visited)
        return visited.toList()
    }
}