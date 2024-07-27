package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class SingleDeleteAction(override val node: GumTree) : Action(node, "DELETE")