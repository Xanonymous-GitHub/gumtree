package tw.xcc.gumtree.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tw.xcc.gumtree.api.tree.TreeMappingStorage

/**
 * The storage for saving the references of the mapping between two GumTrees. (Left and Right)
 * */
class MappingStorage : TreeMappingStorage<GumTree> {
    private val mappingLR = mutableMapOf<GumTree, GumTree>()
    private val mappingRL = mutableMapOf<GumTree, GumTree>()

    private val mutex = Mutex()

    val size: Int by lazy {
        runBlocking(Dispatchers.Default) {
            calculateSizeImpl()
        }
    }

    private suspend fun calculateSizeImpl() =
        mutex.withLock {
            if (mappingLR.size != mappingRL.size) {
                throw IllegalStateException("The size of the mappingLR and mappingRL should be the same.")
            }
            mappingLR.size
        }

    private suspend fun addMappingImpl(mapping: Pair<GumTree, GumTree>) =
        mutex.withLock {
            mappingLR[mapping.first] = mapping.second
            mappingRL[mapping.second] = mapping.first
        }

    private suspend fun addMappingRecursivelyImpl(mapping: Pair<GumTree, GumTree>) {
        addMappingImpl(mapping)
        mapping.first.getChildren().zip(mapping.second.getChildren()).forEach {
            addMappingRecursivelyImpl(it)
        }
    }

    private suspend fun removeMappingImpl(mapping: Pair<GumTree, GumTree>) {
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

        mutex.withLock {
            mappingLR.remove(mapping.first)
            mappingRL.remove(mapping.second)
        }
    }

    private suspend fun extractMappedTreeOf(
        tree: GumTree,
        mapping: Map<GumTree, GumTree>
    ): GumTree? =
        mutex.withLock {
            mapping[tree]
        }

    private suspend fun isMappingExistsIn(
        mapping: Map<GumTree, GumTree>,
        tree: GumTree
    ): Boolean =
        mutex.withLock {
            mapping.containsKey(tree)
        }

    private suspend fun isAnyTreeNotExistsIn(
        mapping: Map<GumTree, GumTree>,
        trees: Iterable<GumTree>
    ): Boolean =
        mutex.withLock {
            trees.any { !mapping.containsKey(it) }
        }

    private suspend fun hasUnMappedDescendent(
        tree: GumTree,
        mapping: Map<GumTree, GumTree>
    ): Boolean =
        mutex.withLock {
            tree.descendents.any { !mapping.containsKey(it) }
        }

    private suspend fun areBothUnMappedImpl(mapping: Pair<GumTree, GumTree>): Boolean =
        mutex.withLock {
            !mappingLR.containsKey(mapping.first) && !mappingRL.containsKey(mapping.second)
        }

    override fun addMappingOf(mapping: Pair<GumTree, GumTree>) =
        runBlocking(Dispatchers.Default) {
            addMappingImpl(mapping)
        }

    override fun addMappingRecursivelyOf(mapping: Pair<GumTree, GumTree>) =
        runBlocking(Dispatchers.Default) {
            addMappingRecursivelyImpl(mapping)
        }

    override fun removeMappingOf(mapping: Pair<GumTree, GumTree>) =
        runBlocking(Dispatchers.Default) {
            removeMappingImpl(mapping)
        }

    override fun getMappingOfLeft(left: GumTree): GumTree? =
        runBlocking(Dispatchers.Default) {
            extractMappedTreeOf(left, mappingLR)
        }

    override fun getMappingOfRight(right: GumTree): GumTree? =
        runBlocking(Dispatchers.Default) {
            extractMappedTreeOf(right, mappingRL)
        }

    override fun isLeftMapped(left: GumTree): Boolean =
        runBlocking(Dispatchers.Default) {
            isMappingExistsIn(mappingLR, left)
        }

    override fun isAnyOfLeftsUnMapped(lefts: Iterable<GumTree>): Boolean =
        runBlocking(Dispatchers.Default) {
            isAnyTreeNotExistsIn(mappingLR, lefts)
        }

    override fun isRightMapped(right: GumTree): Boolean =
        runBlocking(Dispatchers.Default) {
            isMappingExistsIn(mappingRL, right)
        }

    override fun isAnyOfRightsUnMapped(rights: Iterable<GumTree>): Boolean =
        runBlocking(Dispatchers.Default) {
            isAnyTreeNotExistsIn(mappingRL, rights)
        }

    override fun areBothUnMapped(mapping: Pair<GumTree, GumTree>): Boolean =
        runBlocking(Dispatchers.Default) {
            areBothUnMappedImpl(mapping)
        }

    override fun hasUnMappedDescendentOfLeft(left: GumTree): Boolean =
        runBlocking(Dispatchers.Default) {
            hasUnMappedDescendent(left, mappingLR)
        }

    override fun hasUnMappedDescendentOfRight(right: GumTree): Boolean =
        runBlocking(Dispatchers.Default) {
            hasUnMappedDescendent(right, mappingRL)
        }
}