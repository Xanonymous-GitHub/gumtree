package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleUpdate(override val node: GumTree, val value: String) : Action(node, "UPDATE")