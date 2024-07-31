package matchers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.UniversalMatcher
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class UniversalMatcherTest {
    private lateinit var universalMatcherTest: UniversalMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        universalMatcherTest = UniversalMatcher()
        storage = MappingStorage()
    }

    @Test
    fun `test match with real-world tree 1`() {
        val tree1 = realTree1.first
        val tree2 = realTree1.second

        val expectNonMappedDescendentsInTree2 = setOf(17..23).flatten()

        runBlocking {
            // When
            universalMatcherTest.match(tree1, tree2, storage)

            // Then
            val tree1Nodes = tree1.preOrdered()
            val tree2Nodes = tree2.preOrdered()

            for (tree1Idx in tree1Nodes.indices) {
                val tree1Node = tree1Nodes[tree1Idx]
                assertTrue(storage.isLeftMapped(tree1Node))
            }

            for (tree2Idx in tree2Nodes.indices) {
                val tree2Node = tree2Nodes[tree2Idx]
                if (tree2Idx in expectNonMappedDescendentsInTree2) {
                    assertFalse(storage.isRightMapped(tree2Node))
                }
            }
        }
    }
}