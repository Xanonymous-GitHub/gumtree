package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class TreeAction(
    override val node: GumTree,
    override val name: String
) : Action(node, name)