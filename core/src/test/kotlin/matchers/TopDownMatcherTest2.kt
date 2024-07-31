package matchers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.gumTree
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class TopDownMatcherTest2 {
    private lateinit var topDownMatcher: GreedyTopDownMatcher
    private lateinit var storage: MappingStorage

    @BeforeTest
    fun setUp() {
        topDownMatcher = GreedyTopDownMatcher(minHeight = 0)
        storage = MappingStorage()
    }

    @Test
    fun `test match with 3-layer-tree 1`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1") {
                    child("child1.1").also { answerGrid.first.add(it) }
                    child("child1.2").also { answerGrid.first.add(it) }
                }
                child("child2") {
                    child("child2.1").also { answerGrid.first.add(it) }
                    child("child2.2").also { answerGrid.first.add(it) }
                }
            }
        val tree2 =
            gumTree("root") {
                child("child1") {
                    child("child1.1").also { answerGrid.second.add(it) }
                    child("child1.2").also { answerGrid.second.add(it) }
                }
                child("child2") {
                    child("child2.1").also { answerGrid.second.add(it) }
                    child("child2.2").also { answerGrid.second.add(it) }
                }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertTrue(storage.has(tree1 to tree2))
            assertTrue(storage.has(answerGrid.first[0] to answerGrid.second[0]))
            assertTrue(storage.has(answerGrid.first[1] to answerGrid.second[1]))
            assertTrue(storage.has(answerGrid.first[2] to answerGrid.second[2]))
            assertTrue(storage.has(answerGrid.first[3] to answerGrid.second[3]))
        }
    }

    @Test
    fun `test match with 4-layer-tree 2`() {
        val answerGrid = mutableListOf<GumTree>() to mutableListOf<GumTree>()

        // Given
        val tree1 =
            gumTree("root") {
                child("child1") {
                    child("child1.1") {
                        child("child1.1.1").also { answerGrid.first.add(it) }
                    }.also { answerGrid.first.add(it) }
                    child("child1.2") {
                        child("child1.2.1").also { answerGrid.first.add(it) }
                    }.also { answerGrid.first.add(it) }
                }
                child("child2") {
                    child("child2.1") {
                        child("child2.1.1").also { answerGrid.first.add(it) }
                    }.also { answerGrid.first.add(it) }
                    child("child2.2") {
                        child("child2.2.1").also { answerGrid.first.add(it) }
                    }.also { answerGrid.first.add(it) }
                }
            }
        val tree2 =
            gumTree("root") {
                child("child1") {
                    child("child1.1") {
                        child("child1.1.1").also { answerGrid.second.add(it) }
                    }.also { answerGrid.second.add(it) }
                    child("child1.2") {
                        child("child1.2.1").also { answerGrid.second.add(it) }
                    }.also { answerGrid.second.add(it) }
                }
                child("child2") {
                    child("child2.1") {
                        child("child2.1.1").also { answerGrid.second.add(it) }
                    }.also { answerGrid.second.add(it) }
                    child("child2.2") {
                        child("child2.2.1").also { answerGrid.second.add(it) }
                    }.also { answerGrid.second.add(it) }
                }
            }

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertTrue(storage.has(tree1 to tree2))
            for (i in answerGrid.first.indices) {
                assertTrue(storage.has(answerGrid.first[i] to answerGrid.second[i]))
            }
        }
    }

    @Test
    fun `test match with 4-layer-tree 3`() {
        val answerGrid = mutableMapOf<String, GumTree>() to mutableMapOf<String, GumTree>()

        // Given
        val tree1 =
            gumTree("a") {
                child("b") {
                    child("e").also { answerGrid.first["e"] = it }
                }.also { answerGrid.first["b"] = it }
                child("c") {
                    child("f").also { answerGrid.first["f"] = it }
                    child("g").also { answerGrid.first["g"] = it }
                }.also { answerGrid.first["c"] = it }
                child("d").also { answerGrid.first["d"] = it }
            }.also { answerGrid.first["a"] = it }

        val tree2 =
            gumTree("a") {
                child("c") {
                    child("f").also { answerGrid.second["f"] = it }
                    child("g").also { answerGrid.second["g"] = it }
                }.also { answerGrid.second["c"] = it }
                child("b") {
                    child("e") {
                        child("h").also { answerGrid.second["h"] = it }
                    }.also { answerGrid.second["e"] = it }
                }.also { answerGrid.second["b"] = it }
                child("d").also { answerGrid.second["d"] = it }
            }.also { answerGrid.second["a"] = it }

        val expectMatching = setOf("c", "f", "g", "d")

        runBlocking {
            // When
            topDownMatcher.match(tree1, tree2, storage)

            // Then
            assertFalse(storage.has(tree1 to tree2))
            for (key in expectMatching) {
                assertTrue(storage.has(answerGrid.first[key]!! to answerGrid.second[key]!!))
            }

            for (key in answerGrid.first.keys - expectMatching) {
                assertFalse(storage.has(answerGrid.first[key]!! to answerGrid.second[key]!!))
            }
        }
    }
}