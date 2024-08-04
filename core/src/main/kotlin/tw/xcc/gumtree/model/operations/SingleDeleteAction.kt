package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleDeleteAction(override val node: GumTree) : Action(node, "DELETE") {
    override val oldInfo: GumTree.Info = node.info
    override val newInfo: GumTree.Info? = null
}