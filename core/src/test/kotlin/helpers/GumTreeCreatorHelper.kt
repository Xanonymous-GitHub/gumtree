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
    text: String,
    line: Int,
    posOfLine: Int
) {
    private val root by lazy {
        GumTree(
            GumTree.Info(
                type = TreeType(grammarName),
                text = text,
                line = line,
                posOfLine = posOfLine
            )
        )
    }

    @TestOnly
    internal inline fun child(
        grammarName: String,
        text: String = "",
        line: Int = -1,
        posOfLine: Int = -1,
        block: GumTreeBuilder.() -> Unit
    ): GumTree {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val builder = GumTreeBuilder(grammarName, text, line, posOfLine)
        builder.block()

        return builder.build().also { root.addChild(it) }
    }

    @TestOnly
    internal fun child(
        grammarName: String,
        text: String = "",
        line: Int = -1,
        posOfLine: Int = -1
    ): GumTree =
        GumTree(
            GumTree.Info(
                type = TreeType(grammarName),
                line = line,
                posOfLine = posOfLine,
                text = text
            )
        ).also { root.addChild(it) }

    internal fun build(): GumTree = root
}

@TestOnly
@OptIn(ExperimentalContracts::class)
internal inline fun gumTree(
    grammarName: String,
    text: String = "",
    line: Int = -1,
    posOfLine: Int = -1,
    block: GumTreeBuilder.() -> Unit = {}
): GumTree {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val builder = GumTreeBuilder(grammarName, text, line, posOfLine)
    builder.block()
    return builder.build()
}