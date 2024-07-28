package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class TreeAdditionAction(
    override val node: GumTree,
    override val parent: GumTree,
    override val pos: Int,
    override val name: String
) : AdditionAction(node, parent, pos, name)