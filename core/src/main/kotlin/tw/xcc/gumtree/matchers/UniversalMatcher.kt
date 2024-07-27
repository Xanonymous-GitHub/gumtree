package tw.xcc.gumtree.matchers

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.api.tree.TreeMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.GumTreeView
import tw.xcc.gumtree.model.MappingStorage
import tw.xcc.gumtree.model.NonFrozenGumTreeCachePool

class UniversalMatcher : TreeMatcher<GumTree> {
    override suspend fun match(
        tree1: GumTree,
        tree2: GumTree
    ): TreeMappingStorage<GumTree> = match(tree1, tree2, MappingStorage())

    override suspend fun match(
        tree1: GumTree,
        tree2: GumTree,
        storage: TreeMappingStorage<GumTree>
    ): TreeMappingStorage<GumTree> =
        coroutineScope {
            val realTreePool = NonFrozenGumTreeCachePool
            val saveRealJob1 = launch { realTreePool.putAll(tree1.preOrdered().associateBy { it.id }) }
            val saveRealJob2 = launch { realTreePool.putAll(tree2.preOrdered().associateBy { it.id }) }

            val tree1View = async { GumTreeView.frozeEntireTreeFrom(tree1) }
            val tree2View = async { GumTreeView.frozeEntireTreeFrom(tree2) }

            val topDownMatcher = GreedyTopDownMatcher()
            val bottomUpMatcher = BottomUpMatcher()

            listOf(saveRealJob1, saveRealJob2).joinAll()
            topDownMatcher.match(tree1View.await(), tree2View.await(), storage)
            bottomUpMatcher.match(tree1View.await(), tree2View.await(), storage)

            realTreePool.clear()
            return@coroutineScope storage
        }
}