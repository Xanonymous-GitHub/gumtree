package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.TreeMappingStorage
import java.util.concurrent.ConcurrentHashMap

/**
 * The storage for saving the references of the mapping between two GumTrees. (Left and Right)
 * */
class MappingStorage : TreeMappingStorage<GumTree> {
    private val mappingLR = ConcurrentHashMap<String, GumTree>()
    private val mappingRL = ConcurrentHashMap<String, GumTree>()

    val size: Int
        get() = calculateSizeImpl()

    private fun calculateSizeImpl() =
        synchronized(this) {
            if (mappingLR.size != mappingRL.size) {
                throw IllegalStateException("The size of the mappingLR and mappingRL should be the same.")
            }
            mappingLR.size
        }

    private fun addMappingImpl(mapping: Pair<GumTree, GumTree>) =
        synchronized(this) {
            mappingLR[mapping.first.id] = mapping.second
            mappingRL[mapping.second.id] = mapping.first
        }

    private fun addMappingRecursivelyImpl(mapping: Pair<GumTree, GumTree>) {
        addMappingImpl(mapping)
        mapping.first.getChildren().zip(mapping.second.getChildren()).forEach {
            addMappingRecursivelyImpl(it)
        }
    }

    private fun hasImpl(mapping: Pair<GumTree, GumTree>): Boolean =
        synchronized(this) {
            return mappingLR[mapping.first.id] == mapping.second && mappingRL[mapping.second.id] == mapping.first
        }

    private fun removeMappingImpl(mapping: Pair<GumTree, GumTree>) {
        if (mappingLR[mapping.first.id]?.id != mapping.second.id) {
            throw IllegalArgumentException(
                "The mapping should be existed, found L[${mapping.first}] -> R[${mappingLR[mapping.first.id]}]"
            )
        }

        if (mappingRL[mapping.second.id]?.id != mapping.first.id) {
            throw IllegalArgumentException(
                "The mapping should be existed, found R[${mapping.second}] -> L[${mappingRL[mapping.second.id]}]"
            )
        }

        synchronized(this) {
            mappingLR.remove(mapping.first.id)
            mappingRL.remove(mapping.second.id)
        }
    }

    private fun extractMappedTreeOf(
        tree: GumTree,
        mapping: Map<String, GumTree>
    ): GumTree? =
        synchronized(this) {
            mapping[tree.id]
        }

    private fun isMappingExistsIn(
        mapping: Map<String, GumTree>,
        tree: GumTree
    ): Boolean =
        synchronized(this) {
            mapping.containsKey(tree.id)
        }

    private fun isAnyTreeNotExistsIn(
        mapping: Map<String, GumTree>,
        trees: Iterable<GumTree>
    ): Boolean =
        synchronized(this) {
            trees.any { !mapping.containsKey(it.id) }
        }

    private fun hasUnMappedDescendent(
        tree: GumTree,
        mapping: Map<String, GumTree>
    ): Boolean =
        synchronized(this) {
            tree.descendents.any { !mapping.containsKey(it.id) }
        }

    private fun areBothUnMappedImpl(mapping: Pair<GumTree, GumTree>): Boolean =
        synchronized(this) {
            !mappingLR.containsKey(mapping.first.id) && !mappingRL.containsKey(mapping.second.id)
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

    override fun has(mapping: Pair<GumTree, GumTree>): Boolean = hasImpl(mapping)

    override fun hasUnMappedDescendentOfLeft(left: GumTree): Boolean = hasUnMappedDescendent(left, mappingLR)

    override fun hasUnMappedDescendentOfRight(right: GumTree): Boolean = hasUnMappedDescendent(right, mappingRL)

    override fun clone(): MappingStorage =
        synchronized(this) {
            val newStorage = MappingStorage()
            this.mappingRL.forEach {
                newStorage.mappingRL[it.key] = GumTree(it.value)
            }
            this.mappingLR.forEach {
                newStorage.mappingLR[it.key] = GumTree(it.value)
            }
            return newStorage
        }
}