package matchers

import helpers.gumTree
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class TopDownMatcherTest1 {
    private lateinit var topDownMatcher: GreedyTopDownMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        topDownMatcher = GreedyTopDownMatcher(minHeight = 0)
        storage = MappingStorage()
    }

    @Test
    fun `test match with simple tree 1`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1").also { answerGrid.first.add(it) }
                child("child2").also { answerGrid.first.add(it) }
            }
        val tree2 =
            gumTree("root") {
                child("child1").also { answerGrid.second.add(it) }
                child("child2").also { answerGrid.second.add(it) }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertTrue(storage.has(tree1 to tree2))
            assertTrue(storage.has(answerGrid.first[0] to answerGrid.second[0]))
            assertTrue(storage.has(answerGrid.first[1] to answerGrid.second[1]))
        }
    }

    @Test
    fun `test match with simple tree 2`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1") {
                    child("child2").also { answerGrid.first.add(it) }
                }.also { answerGrid.first.add(it) }
            }
        val tree2 =
            gumTree("root") {
                child("child1") {
                    child("child2").also { answerGrid.second.add(it) }
                }.also { answerGrid.second.add(it) }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertTrue(storage.has(tree1 to tree2))
            assertTrue(storage.has(answerGrid.first[0] to answerGrid.second[0]))
            assertTrue(storage.has(answerGrid.first[1] to answerGrid.second[1]))
        }
    }

    @Test
    fun `test match with simple tree 3, different tree`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1").also { answerGrid.first.add(it) }
                child("child2").also { answerGrid.first.add(it) }
            }
        val tree2 =
            gumTree("root") {
                child("child1").also { answerGrid.second.add(it) }
                child("child3").also { answerGrid.second.add(it) }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertFalse(storage.has(tree1 to tree2))
            assertTrue(storage.has(answerGrid.first[0] to answerGrid.second[0]))
            assertFalse(storage.has(answerGrid.first[1] to answerGrid.second[1]))
        }
    }

    @Test
    fun `test match with simple tree 4, different tree`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1") {
                    child("child2").also { answerGrid.first.add(it) }
                }.also { answerGrid.first.add(it) }
            }
        val tree2 =
            gumTree("root") {
                child("child2") {
                    child("child3").also { answerGrid.second.add(it) }
                }.also { answerGrid.second.add(it) }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertFalse(storage.has(tree1 to tree2))
            assertFalse(storage.has(answerGrid.first[0] to answerGrid.second[0]))
            assertFalse(storage.has(answerGrid.first[1] to answerGrid.second[1]))
        }
    }

    @Test
    fun `test match with simple tree 5, different structure`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1").also { answerGrid.first.add(it) }
                child("child2").also { answerGrid.first.add(it) }
            }
        val tree2 =
            gumTree("root") {
                child("child1") {
                    child("child2").also {
                        answerGrid.second.add(it) // index 0
                    }
                }.also {
                    answerGrid.second.add(it) // index 1
                }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertFalse(storage.has(tree1 to tree2))
            assertFalse(storage.has(answerGrid.first[0] to answerGrid.second[1]))
            assertTrue(storage.has(answerGrid.first[1] to answerGrid.second[0]))
        }
    }
}