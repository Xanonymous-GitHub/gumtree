package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class Action(open val node: GumTree, open val name: String)