package tw.xcc.gumtree.model.operations

import tw.xcc.gumtree.model.GumTree
import java.io.Serializable

abstract class Action(open val node: GumTree, open val name: String) : Serializable {
    abstract val oldInfo: GumTree.Info?
    abstract val newInfo: GumTree.Info?
}