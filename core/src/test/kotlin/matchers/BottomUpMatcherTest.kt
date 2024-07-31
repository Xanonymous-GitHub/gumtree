package matchers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.BottomUpMatcher
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class BottomUpMatcherTest {
    private lateinit var topDownMatcher: GreedyTopDownMatcher
    private lateinit var bottomUpMatcher: BottomUpMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        topDownMatcher = GreedyTopDownMatcher()
        bottomUpMatcher = BottomUpMatcher()
        storage = MappingStorage()
    }

    @Test
    fun `test match with real-world tree 1`() {
        val tree1 = realTree1.first
        val tree2 = realTree1.second

        val expectMappedDescendents =
            setOf(
                0 to 0,
                1 to 1,
                2 to 2,
                3 to 3,
                4 to 4,
                5 to 5,
                8 to 8,
                12 to 12,
                13 to 13
            )

        runBlocking {
            // When
            val middleStageStore = topDownMatcher.match(tree1, tree2, storage).clone()
            bottomUpMatcher.match(tree1, tree2, storage)

            // Then
            val tree1Nodes = tree1.preOrdered()
            val tree2Nodes = tree2.preOrdered()

            for ((i, j) in expectMappedDescendents) {
                val expectMapping = tree1Nodes[i] to tree2Nodes[j]
                assertTrue(storage.has(expectMapping) && !middleStageStore.has(expectMapping))
            }
        }
    }
}