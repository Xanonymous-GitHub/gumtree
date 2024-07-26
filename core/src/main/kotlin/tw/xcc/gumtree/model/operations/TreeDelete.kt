package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

data class TreeDelete(override val node: GumTree) : TreeAction(node, "DELETE-TREE")