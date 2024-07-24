package tw.xcc.gumtree.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import tw.xcc.gumtree.api.tree.TreeMappingStorage

/**
 * The storage for saving the references of the mapping between two GumTrees. (Left and Right)
 * */
class MappingStorage : TreeMappingStorage<GumTree> {
    private val mappingLR = mutableMapOf<GumTree, GumTree>()
    private val mappingRL = mutableMapOf<GumTree, GumTree>()

    val size: Int by lazy {
        runBlocking(Dispatchers.Default) {
            calculateSizeImpl()
        }
    }

    private fun calculateSizeImpl() =
        synchronized(this) {
            if (mappingLR.size != mappingRL.size) {
                throw IllegalStateException("The size of the mappingLR and mappingRL should be the same.")
            }
            mappingLR.size
        }

    private fun addMappingImpl(mapping: Pair<GumTree, GumTree>) =
        synchronized(this) {
            mappingLR[mapping.first] = mapping.second
            mappingRL[mapping.second] = mapping.first
        }

    private fun addMappingRecursivelyImpl(mapping: Pair<GumTree, GumTree>) {
        addMappingImpl(mapping)
        mapping.first.getChildren().zip(mapping.second.getChildren()).forEach {
            addMappingRecursivelyImpl(it)
        }
    }

    private fun removeMappingImpl(mapping: Pair<GumTree, GumTree>) {
        if (mappingLR[mapping.first] != mapping.second) {
            throw IllegalArgumentException(
                "The mapping should be existed, found L[${mapping.first}] -> R[${mappingLR[mapping.first]}]"
            )
        }

        if (mappingRL[mapping.second] != mapping.first) {
            throw IllegalArgumentException(
                "The mapping should be existed, found R[${mapping.second}] -> L[${mappingRL[mapping.second]}]"
            )
        }

        synchronized(this) {
            mappingLR.remove(mapping.first)
            mappingRL.remove(mapping.second)
        }
    }

    private fun extractMappedTreeOf(
        tree: GumTree,
        mapping: Map<GumTree, GumTree>
    ): GumTree? =
        synchronized(this) {
            mapping[tree]
        }

    private fun isMappingExistsIn(
        mapping: Map<GumTree, GumTree>,
        tree: GumTree
    ): Boolean =
        synchronized(this) {
            mapping.containsKey(tree)
        }

    private fun isAnyTreeNotExistsIn(
        mapping: Map<GumTree, GumTree>,
        trees: Iterable<GumTree>
    ): Boolean =
        synchronized(this) {
            trees.any { !mapping.containsKey(it) }
        }

    private fun hasUnMappedDescendent(
        tree: GumTree,
        mapping: Map<GumTree, GumTree>
    ): Boolean =
        synchronized(this) {
            tree.descendents.any { !mapping.containsKey(it) }
        }

    private fun areBothUnMappedImpl(mapping: Pair<GumTree, GumTree>): Boolean =
        synchronized(this) {
            !mappingLR.containsKey(mapping.first) && !mappingRL.containsKey(mapping.second)
        }

    override fun addMappingOf(mapping: Pair<GumTree, GumTree>) = addMappingImpl(mapping)

    override fun addMappingRecursivelyOf(mapping: Pair<GumTree, GumTree>) = addMappingRecursivelyImpl(mapping)

    override fun removeMappingOf(mapping: Pair<GumTree, GumTree>) = removeMappingImpl(mapping)

    override fun getMappingOfLeft(left: GumTree): GumTree? = extractMappedTreeOf(left, mappingLR)

    override fun getMappingOfRight(right: GumTree): GumTree? = extractMappedTreeOf(right, mappingRL)

    override fun isLeftMapped(left: GumTree): Boolean = isMappingExistsIn(mappingLR, left)

    override fun isAnyOfLeftsUnMapped(lefts: Iterable<GumTree>): Boolean = isAnyTreeNotExistsIn(mappingLR, lefts)

    override fun isRightMapped(right: GumTree): Boolean = isMappingExistsIn(mappingRL, right)

    override fun isAnyOfRightsUnMapped(rights: Iterable<GumTree>): Boolean = isAnyTreeNotExistsIn(mappingRL, rights)

    override fun areBothUnMapped(mapping: Pair<GumTree, GumTree>): Boolean = areBothUnMappedImpl(mapping)

    override fun hasUnMappedDescendentOfLeft(left: GumTree): Boolean = hasUnMappedDescendent(left, mappingLR)

    override fun hasUnMappedDescendentOfRight(right: GumTree): Boolean = hasUnMappedDescendent(right, mappingRL)
}