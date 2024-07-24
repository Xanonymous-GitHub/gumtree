package tw.xcc.gumtree.matchers

import tw.xcc.gumtree.model.GumTree

private typealias PairOfIsomorphicSetInternal = Pair<MutableSet<GumTree>, MutableSet<GumTree>>
typealias PairOfIsomorphicSet = Pair<Set<GumTree>, Set<GumTree>>
typealias PairsOfMappedTree = List<Pair<GumTree, GumTree>>

class HashSimilarityOrganizer(
    memo1: Map<String, Long>,
    memo2: Map<String, Long>,
    mappings: PairsOfMappedTree
) {
    private val hashToTreePairs = mutableMapOf<Long, PairOfIsomorphicSetInternal>()

    val uniqueIsomorphicMappings: Sequence<PairOfIsomorphicSet> by lazy {
        hashToTreePairs.values.asSequence().filter {
            it.first.size == 1 && it.second.size == 1
        }
    }

    val nonUniqueIsomorphicMappings: Sequence<PairOfIsomorphicSet> by lazy {
        hashToTreePairs.values.asSequence().filter {
            (it.first.size > 1 && it.second.size >= 1) || (it.first.size >= 1 && it.second.size > 1)
        }
    }

    val nonIsomorphicMappings: Sequence<PairOfIsomorphicSet> by lazy {
        hashToTreePairs.values.asSequence().filter {
            it.first.size == 0 || it.second.size == 0
        }
    }

    private fun newIsomorphicMappings(
        memo1: Map<String, Long>,
        memo2: Map<String, Long>,
        mappings: PairsOfMappedTree
    ) {
        mappings.forEach { mapping ->
            val hashOfLeft = memo1[mapping.first.id]
            val hashOfRight = memo2[mapping.second.id]
            if (hashOfLeft == null || hashOfRight == null) {
                throw NoSuchElementException("The hash of the tree should be existed in its memo.")
            }

            if (hashToTreePairs.containsKey(hashOfLeft)) {
                hashToTreePairs[hashOfLeft]!!.first.add(mapping.first)
            } else {
                hashToTreePairs[hashOfLeft] = PairOfIsomorphicSetInternal(mutableSetOf(mapping.first), mutableSetOf())
            }

            if (hashToTreePairs.containsKey(hashOfRight)) {
                hashToTreePairs[hashOfRight]!!.second.add(mapping.second)
            } else {
                hashToTreePairs[hashOfRight] = PairOfIsomorphicSetInternal(mutableSetOf(), mutableSetOf(mapping.second))
            }
        }
    }

    init {
        newIsomorphicMappings(memo1, memo2, mappings)
    }
}