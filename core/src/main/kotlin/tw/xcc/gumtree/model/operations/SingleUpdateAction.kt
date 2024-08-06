package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType

data class SingleUpdateAction(
    override val node: GumTree,
    private val oldType: TreeType,
    private val oldText: String
) : Action(node, "UPDATE") {
    override val oldInfo: GumTree.Info =
        node.info.copy(
            type = oldType,
            text = oldText
        )
    override val newInfo: GumTree.Info = node.info.copy()
}