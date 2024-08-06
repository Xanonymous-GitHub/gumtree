package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleInsertAction(
    override val node: GumTree,
    override val parent: GumTree,
    override val pos: Int
) : AdditionAction(node, parent, pos, "INSERT") {
    override val oldInfo: GumTree.Info? = null
    override val newInfo: GumTree.Info = node.info.copy()
}