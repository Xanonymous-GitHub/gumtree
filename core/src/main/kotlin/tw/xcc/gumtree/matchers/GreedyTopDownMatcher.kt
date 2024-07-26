package tw.xcc.gumtree.matchers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.api.tree.TreeMatcher
import tw.xcc.gumtree.helper.createHashMemoOf
import tw.xcc.gumtree.helper.crossProductOf
import tw.xcc.gumtree.matchers.comparator.AbsolutePositionDistanceComparator
import tw.xcc.gumtree.matchers.comparator.AncestorsSimilarityComparator
import tw.xcc.gumtree.matchers.comparator.PositionOfParentsSimilarityComparator
import tw.xcc.gumtree.matchers.comparator.SiblingsSimilarityComparator
import tw.xcc.gumtree.matchers.comparator.SubtreeSizeComparator
import tw.xcc.gumtree.matchers.comparator.TextualPositionDistanceComparator
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.HeightPriorityList
import tw.xcc.gumtree.model.MappingStorage

class GreedyTopDownMatcher(private val minHeight: Int = 1) : TreeMatcher<GumTree> {
    private fun processNonUniqueMappings(
        mappedPacks: List<PairOfIsomorphicSet>,
        storage: TreeMappingStorage<GumTree>
    ) {
        mappedPacks.sortedWith(SubtreeSizeComparator())
            .forEach { pairOfSet ->
                val mappings = pairOfSet.first crossProductOf pairOfSet.second
                mappings.sortedWith(AdvancedDiceComparator(storage))
                    .forEach { mapping ->
                        if (storage.areBothUnMapped(mapping)) {
                            storage.addMappingRecursivelyOf(mapping)
                        }
                    }
            }
    }

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
            val list1 = HeightPriorityList(minHeight).also { it.push(tree1) }
            val list2 = HeightPriorityList(minHeight).also { it.push(tree2) }

            val memo1 = createHashMemoOf(tree1)
            val memo2 = createHashMemoOf(tree2)

            val nonUniqueMappings = mutableListOf<PairOfIsomorphicSet>()

            while (list1 hasBeenSynchronizedTo list2) {
                val h1 = list1.pop()
                val h2 = list2.pop()
                val organizer = HashSimilarityOrganizer(memo1, memo2, h1 crossProductOf h2)

                val uniqueJob =
                    launch {
                        organizer.uniqueIsomorphicMappings.forEach { pairOfSet ->
                            storage.addMappingRecursivelyOf(pairOfSet.first.single() to pairOfSet.second.single())
                        }
                    }
                val nonUniqueJob =
                    launch {
                        nonUniqueMappings.addAll(organizer.nonUniqueIsomorphicMappings)
                    }
                val nonMappedJob =
                    launch {
                        organizer.nonIsomorphicMappings.forEach { pairOfSet ->
                            pairOfSet.first.forEach { list1.open(it) }
                            pairOfSet.second.forEach { list2.open(it) }
                        }
                    }

                joinAll(uniqueJob, nonUniqueJob, nonMappedJob)
            }

            processNonUniqueMappings(nonUniqueMappings, storage)

            return@coroutineScope storage
        }

    private inner class AdvancedDiceComparator(
        storage: TreeMappingStorage<GumTree>
    ) : Comparator<Pair<GumTree, GumTree>> {
        private val siblingsSimilarityComparator = SiblingsSimilarityComparator(storage)
        private val ancestorsSimilarityComparator = AncestorsSimilarityComparator()
        private val positionOfParentsSimilarityComparator = PositionOfParentsSimilarityComparator()
        private val textualPositionDistanceComparator = TextualPositionDistanceComparator()
        private val absolutePositionDistanceComparator = AbsolutePositionDistanceComparator()

        override fun compare(
            m1: Pair<GumTree, GumTree>,
            m2: Pair<GumTree, GumTree>
        ): Int {
            siblingsSimilarityComparator.compare(m1, m2).takeIf { it != 0 }?.let { return it }
            ancestorsSimilarityComparator.compare(m1, m2).takeIf { it != 0 }?.let { return it }
            positionOfParentsSimilarityComparator.compare(m1, m2).takeIf { it != 0 }?.let { return it }
            textualPositionDistanceComparator.compare(m1, m2).takeIf { it != 0 }?.let { return it }
            return absolutePositionDistanceComparator.compare(m1, m2)
        }
    }
}