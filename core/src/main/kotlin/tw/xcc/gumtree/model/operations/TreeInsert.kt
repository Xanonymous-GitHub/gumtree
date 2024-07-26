package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeInsert(
    override val pos: Int,
    override val node: GumTree,
    override val parent: GumTree,
    override val name: String
) : TreeAdditionAction(pos, parent, node, name)