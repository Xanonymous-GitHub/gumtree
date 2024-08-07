package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeDeleteAction(override val node: GumTree) : TreeAction(node, "TREE-DELETE") {
    override val oldInfo: GumTree.Info = node.info.copy()
    override val newInfo: GumTree.Info? = null
}