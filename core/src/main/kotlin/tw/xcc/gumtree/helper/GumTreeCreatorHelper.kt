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
internal class GumTreeBuilder(grammarName: String, label: String, pos: Int, length: Int) {
    private val root by lazy { GumTree(TreeType(grammarName), label, pos, length) }

    fun child(
        grammarName: String,
        label: String = "",
        pos: Int = -1,
        length: Int = -1,
        block: GumTreeBuilder.() -> Unit
    ) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val child = GumTreeBuilder(grammarName, label, pos, length)
        child.block()
        root.addChild(child.build())
    }

    fun child(
        grammarName: String,
        label: String = "",
        pos: Int = -1,
        length: Int = -1
    ) {
        root.addChild(GumTree(TreeType(grammarName), label, pos, length))
    }

    internal fun build(): GumTree = root
}

@OptIn(ExperimentalContracts::class)
internal fun gumTree(
    grammarName: String,
    label: String = "",
    pos: Int = -1,
    length: Int = -1,
    block: GumTreeBuilder.() -> Unit = {}
): GumTree {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val builder = GumTreeBuilder(grammarName, label, pos, length)
    builder.block()
    return builder.build()
}