package matchers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.gumTree
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class TopDownMatcherOnlyRootTest {
    private lateinit var topDownMatcher: GreedyTopDownMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        topDownMatcher = GreedyTopDownMatcher()
        storage = MappingStorage()
    }

    @Test
    fun `test match with same root node on both side`() {
        // Given
        val tree1 = gumTree("root", "label")
        val tree2 = gumTree("root", "label")

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            // The default minimum height is 1; the root node should not be matched,
            // since they are also the leaf node.
            assertFalse(storage.has(tree1 to tree2))
        }
    }

    @Test
    fun `test match with same root node on both side with minimum height 0`() {
        // Given
        val tree1 = gumTree("root", "label")
        val tree2 = gumTree("root", "label")
        topDownMatcher = GreedyTopDownMatcher(0)

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            // The minimum height is 0; the root node should be matched.
            assertTrue(storage.has(tree1 to tree2))
        }
    }

    @Test
    fun `test match with different root node on both side`() {
        // Given
        val tree1 = gumTree("root1", "label")
        val tree2 = gumTree("root2", "label")

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            // The default minimum height is 1; the root node should not be matched.
            assertFalse(storage.has(tree1 to tree2))
        }
    }

    @Test
    fun `test match with different root node on both side with minimum height 0`() {
        // Given
        val tree1 = gumTree("root1", "label")
        val tree2 = gumTree("root2", "label")
        topDownMatcher = GreedyTopDownMatcher(0)

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            // The minimum height is 0; the root node should not be matched.
            // Because they have different grammar-type
            assertFalse(storage.has(tree1 to tree2))
        }
    }

    @Test
    fun `test match with different root node on both side with minimum height 0 and different label`() {
        // Given
        val tree1 = gumTree("root", "label-1")
        val tree2 = gumTree("root", "label-2")
        topDownMatcher = GreedyTopDownMatcher(0)

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            // The minimum height is 0; the root node should not be matched.
            // Because they have different label
            assertFalse(storage.has(tree1 to tree2))
        }
    }
}