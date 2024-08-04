package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree

abstract class Action(open val node: GumTree, open val name: String) {
    abstract val oldInfo: GumTree.Info?
    abstract val newInfo: GumTree.Info?
}