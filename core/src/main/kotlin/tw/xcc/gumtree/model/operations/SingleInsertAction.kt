package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleInsertAction(
    override val pos: Int,
    override val parent: GumTree,
    override val node: GumTree,
    override val name: String
) : AdditionAction(pos, parent, node, name)