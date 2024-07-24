package tw.xcc.gumtree.helper

import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType

@DslMarker
internal annotation class GumTreeMarker

@GumTreeMarker
internal class GumTreeBuilder(name: String, label: String) {
    private val root by lazy { GumTree(TreeType(name), label) }

    fun child(
        name: String,
        label: String,
        block: GumTreeBuilder.() -> Unit
    ) {
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

internal fun gumTree(
    name: String,
    label: String,
    block: GumTreeBuilder.() -> Unit
): GumTree {
    val builder = GumTreeBuilder(name, label)
    builder.block()
    return builder.build()
}