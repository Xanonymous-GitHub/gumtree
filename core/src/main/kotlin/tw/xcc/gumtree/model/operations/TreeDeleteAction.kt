package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeDeleteAction(override val node: GumTree) : TreeAction(node, "DELETE-TREE") {
    override val oldInfo: GumTree.Info = node.info
    override val newInfo: GumTree.Info? = null
}