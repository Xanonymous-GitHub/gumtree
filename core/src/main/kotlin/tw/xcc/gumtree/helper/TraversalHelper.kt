package tw.xcc.gumtree.helper

import tw.xcc.gumtree.api.tree.Tree
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
private fun <T : Tree> preOrderedImpl(
    tree: T,
    action: (T) -> Unit
) {
    contract {
        callsInPlace(action, InvocationKind.AT_LEAST_ONCE)
    }

    action(tree)
    tree.getChildren().forEach {
        @Suppress("UNCHECKED_CAST")
        preOrderedImpl(it as T, action)
    }
}

@OptIn(ExperimentalContracts::class)
private fun <T : Tree> postOrderedImpl(
    tree: T,
    action: (T) -> Unit
) {
    contract {
        callsInPlace(action, InvocationKind.AT_LEAST_ONCE)
    }

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