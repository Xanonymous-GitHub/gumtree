package tw.xcc.gumtree.helper

import tw.xcc.gumtree.api.tree.Tree

private fun <T : Tree> preOrderedImpl(
    tree: T,
    action: (T) -> Unit
) {
    action(tree)
    tree.getChildren().forEach {
        @Suppress("UNCHECKED_CAST")
        preOrderedImpl(it as T, action)
    }
}

private fun <T : Tree> postOrderedImpl(
    tree: T,
    action: (T) -> Unit
) {
    tree.getChildren().forEach {
        @Suppress("UNCHECKED_CAST")
        postOrderedImpl(it as T, action)
    }
    action(tree)
}

fun <T : Tree> preOrderOf(
    tree: T,
    action: (T) -> Unit
) = synchronized(tree) {
    preOrderedImpl(tree, action)
}

fun <T : Tree> postOrderOf(
    tree: T,
    action: (T) -> Unit
) = synchronized(tree) {
    postOrderedImpl(tree, action)
}