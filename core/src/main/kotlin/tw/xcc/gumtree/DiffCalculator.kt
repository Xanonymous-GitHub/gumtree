package tw.xcc.gumtree

import kotlinx.coroutines.coroutineScope
import tw.xcc.gumtree.editscript.SimplifiedEditScriptGenerator
import tw.xcc.gumtree.matchers.UniversalMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.operations.Action

class DiffCalculator {
    suspend fun computeEditScriptFrom(treePair: Pair<GumTree, GumTree>): List<Action> =
        coroutineScope {
            val matcher = UniversalMatcher()
            val matchedStorage = matcher.match(treePair.first, treePair.second)
            val scriptGenerator = SimplifiedEditScriptGenerator(treePair.first, treePair.second, matchedStorage)
            return@coroutineScope scriptGenerator.generateActions()
        }
}