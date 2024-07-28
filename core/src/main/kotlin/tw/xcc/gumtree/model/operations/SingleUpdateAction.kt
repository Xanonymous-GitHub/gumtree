package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType

data class SingleUpdateAction(
    override val node: GumTree,
    val type: TreeType,
    val label: String
) : Action(node, "UPDATE")