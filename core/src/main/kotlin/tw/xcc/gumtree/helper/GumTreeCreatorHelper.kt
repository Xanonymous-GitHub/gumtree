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
internal class GumTreeBuilder(name: String, label: String) {
    private val root by lazy { GumTree(TreeType(name), label) }

    fun child(
        name: String,
        label: String,
        block: GumTreeBuilder.() -> Unit
    ) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val child = GumTreeBuilder(name, label)
        child.block()
        root.addChild(child.build())
    }

    fun child(
        name: String,
        label: String
    ) {
        root.addChild(GumTree(TreeType(name), label))
    }

    fun build(): GumTree = root
}

@OptIn(ExperimentalContracts::class)
internal fun gumTree(
    name: String,
    label: String,
    block: GumTreeBuilder.() -> Unit
): GumTree {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val builder = GumTreeBuilder(name, label)
    builder.block()
    return builder.build()
}