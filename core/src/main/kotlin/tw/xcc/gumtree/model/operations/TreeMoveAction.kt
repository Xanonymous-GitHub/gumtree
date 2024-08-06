package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeMoveAction(
    override val node: GumTree,
    override val parent: GumTree,
    override val pos: Int,
    private val newLine: Int,
    private val newPosOfLine: Int
) : TreeAdditionAction(node, parent, pos, "TREE-MOVE") {
    override val oldInfo: GumTree.Info = node.info.copy()
    override val newInfo: GumTree.Info =
        node.info.copy(
            line = newLine,
            posOfLine = newPosOfLine
        )
}