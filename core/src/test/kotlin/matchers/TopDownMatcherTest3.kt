package matchers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import shared.realTree1
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class TopDownMatcherTest3 {
    private lateinit var topDownMatcher: GreedyTopDownMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        topDownMatcher = GreedyTopDownMatcher()
        storage = MappingStorage()
    }

    @Test
    fun `test match with real-world tree 1`() {
        val tree1 = realTree1.first
        val tree2 = realTree1.second

        val expectMappedDescendents =
            setOf(
                6 to 6,
                7 to 7,
                9 to 9,
                10 to 10,
                11 to 11,
                14 to 14,
                15 to 15,
                16 to 16,
                17 to 24,
                18 to 25
            )

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            val tree1Nodes = tree1.preOrdered()
            val tree2Nodes = tree2.preOrdered()

            for ((i, j) in expectMappedDescendents) {
                assertTrue(storage.has(tree1Nodes[i] to tree2Nodes[j]))
            }

            for ((tree2Idx, tree2Node) in tree2Nodes.withIndex()) {
                if (tree2Idx < tree1Nodes.size && tree2Idx !in expectMappedDescendents.map { it.second }) {
                    assertFalse(storage.has(tree1Nodes[tree2Idx] to tree2Node))
                }
            }
        }
    }
}