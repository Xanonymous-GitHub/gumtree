package tw.xcc.gumtree.matchers

import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.api.tree.TreeMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.GumTreeView
import tw.xcc.gumtree.model.MappingStorage

class UniversalMatcher : TreeMatcher<GumTree> {
    override fun match(
        tree1: GumTree,
        tree2: GumTree
    ): TreeMappingStorage<GumTree> = match(tree1, tree2, MappingStorage())

    override fun match(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): TreeMappingStorage<GumTree> {
        val tree1View = GumTreeView.frozeEntireTreeFrom(tree1)
        val tree2View = GumTreeView.frozeEntireTreeFrom(tree2)
        val topDownMatcher = GreedyTopDownMatcher()
        val bottomUpMatcher = BottomUpMatcher()
        topDownMatcher.match(tree1View, tree2View, storage)
        bottomUpMatcher.match(tree1View, tree2View, storage)
        return storage
    }
}