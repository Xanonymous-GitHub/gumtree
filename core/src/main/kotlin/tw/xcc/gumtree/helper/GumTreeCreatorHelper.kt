package tw.xcc.gumtree.helper

import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@DslMarker
internal annotation class GumTreeMarker

@OptIn(ExperimentalContracts::class)
@GumTreeMarker
internal class GumTreeBuilder(grammarName: String, label: String) {
    private val root by lazy { GumTree(TreeType(grammarName), label) }

    fun child(
        grammarName: String,
        label: String = "",
        block: GumTreeBuilder.() -> Unit
    ) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val child = GumTreeBuilder(grammarName, label)
        child.block()
        root.addChild(child.build())
    }

    fun child(
        grammarName: String,
        label: String = ""
    ) {
        root.addChild(GumTree(TreeType(grammarName), label))
    }

    internal fun build(): GumTree = root
}

@OptIn(ExperimentalContracts::class)
internal fun gumTree(
    grammarName: String,
    label: String = "",
    block: GumTreeBuilder.() -> Unit
): GumTree {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val builder = GumTreeBuilder(grammarName, label)
    builder.block()
    return builder.build()
}