package helpers

import org.jetbrains.annotations.TestOnly
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@TestOnly
@DslMarker
internal annotation class GumTreeMarker

@TestOnly
@OptIn(ExperimentalContracts::class)
@GumTreeMarker
internal class GumTreeBuilder(
    grammarName: String,
    label: String,
    pos: Int,
    length: Int
) {
    private val root by lazy { GumTree(TreeType(grammarName), label, pos, length) }

    @TestOnly
    internal inline fun child(
        grammarName: String,
        label: String = "",
        pos: Int = -1,
        length: Int = -1,
        block: GumTreeBuilder.() -> Unit
    ): GumTree {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val builder = GumTreeBuilder(grammarName, label, pos, length)
        builder.block()

        return builder.build().also { root.addChild(it) }
    }

    @TestOnly
    internal fun child(
        grammarName: String,
        label: String = "",
        pos: Int = -1,
        length: Int = -1
    ): GumTree = GumTree(TreeType(grammarName), label, pos, length).also { root.addChild(it) }

    internal fun build(): GumTree = root
}

@TestOnly
@OptIn(ExperimentalContracts::class)
internal inline fun gumTree(
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