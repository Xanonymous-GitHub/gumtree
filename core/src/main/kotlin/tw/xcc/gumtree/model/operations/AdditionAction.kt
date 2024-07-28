package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class AdditionAction(
    override val node: GumTree,
    open val parent: GumTree,
    open val pos: Int,
    override val name: String
) : Action(node, name)