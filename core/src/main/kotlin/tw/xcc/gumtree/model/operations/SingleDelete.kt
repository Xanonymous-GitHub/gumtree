package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleDelete(override val node: GumTree) : Action(node, "DELETE")