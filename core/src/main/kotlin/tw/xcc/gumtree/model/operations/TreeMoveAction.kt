package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeMoveAction(
    override val node: GumTree,
    override val parent: GumTree,
    override val pos: Int
) : TreeAdditionAction(node, parent, pos, "TREE-MOVE")