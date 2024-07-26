package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class AdditionAction(
    open val pos: Int,
    open val parent: GumTree,
    override val node: GumTree,
    override val name: String
) : Action(node, name)