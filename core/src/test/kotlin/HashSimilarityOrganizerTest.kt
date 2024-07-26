import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.createHashMemoOf
import tw.xcc.gumtree.helper.crossProductOf
import tw.xcc.gumtree.helper.gumTree
import tw.xcc.gumtree.matchers.HashSimilarityOrganizer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Execution(ExecutionMode.CONCURRENT)
class HashSimilarityOrganizerTest {
    private lateinit var givenOrganizer: HashSimilarityOrganizer

    @BeforeTest
    fun setUp() {
        val leftTree =
            gumTree("1", "label") {
                child("4", "label") {
                    child("5", "label")
                    child("6", "label")
                }
                child("4", "label") {
                    child("5", "label")
                    child("6", "label")
                }
                child("3", "label")
                child("7", "label") {
                    child("8", "label")
                    child("9", "label")
                }
            }

        val rightTree =
            gumTree("2", "label") {
                child("4", "label") {
                    child("5", "label")
                    child("6", "label")
                }
                child("10", "label") {
                    child("7", "label") {
                        child("8", "label")
                        child("9", "label")
                    }
                    child("11", "label")
                }
            }

        val memo1 = runBlocking { createHashMemoOf(leftTree) }
        val memo2 = runBlocking { createHashMemoOf(rightTree) }
        val mappings = leftTree.preOrdered() crossProductOf rightTree.preOrdered()

        givenOrganizer = HashSimilarityOrganizer(memo1, memo2, mappings)
    }

    @Test
    fun `test uniqueIsomorphicMappings`() {
        val actual =
            givenOrganizer.uniqueIsomorphicMappings.toList()
                .map {
                    it.first.map { tree -> tree.type.name }.toSet() to it.second.map { tree -> tree.type.name }.toSet()
                }.toSet()
        val expected =
            setOf(
                setOf("7") to setOf("7"),
                setOf("8") to setOf("8"),
                setOf("9") to setOf("9")
            )
        assertEquals(expected, actual)
    }

    @Test
    fun `test nonUniqueIsomorphicMappings`() {
        val actual =
            givenOrganizer.nonUniqueIsomorphicMappings.toList()
                .map {
                    it.first.map { tree -> tree.type.name }.toSet() to it.second.map { tree -> tree.type.name }.toSet()
                }.toSet()
        val expected =
            setOf(
                setOf("4") to setOf("4"),
                setOf("5") to setOf("5"),
                setOf("6") to setOf("6")
            )
        assertEquals(expected, actual)
    }

    @Test
    fun `test nonIsomorphicMappings`() {
        val actual =
            givenOrganizer.nonIsomorphicMappings.toList()
                .map {
                    it.first.map { tree -> tree.type.name }.toSet() to it.second.map { tree -> tree.type.name }.toSet()
                }.toSet()
        val expected =
            setOf(
                setOf("1") to emptySet(),
                setOf("3") to emptySet(),
                emptySet<String>() to setOf("11"),
                emptySet<String>() to setOf("10"),
                emptySet<String>() to setOf("2")
            )
        assertEquals(expected, actual)
    }
}