import helpers.gumTree
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.createHashMemoOf
import tw.xcc.gumtree.model.GumTree
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class CompareHelperTest {
    private lateinit var givenRoot1: GumTree
    private lateinit var givenRoot2: GumTree

    @BeforeTest
    fun setUp() {
        givenRoot1 =
            gumTree("same") {
                child("same") {
                    child("same")
                    child("same") {
                        child("same")
                        child("same")
                    }
                }
                child("same") {
                    child("same")
                }
                child("same") {
                    child("same") {
                        child("same")
                    }
                    child("same")
                }
            }
        givenRoot2 =
            gumTree("same") {
                child("same") {
                    child("same")
                    child("same") {
                        child("same")
                        child("same")
                    }
                }
                child("same") {
                    child("same")
                }
                child("same") {
                    child("same") {
                        child("same")
                    }
                    child("same")
                }
            }
    }

    @Test
    fun `test isIsomorphic positive`() =
        runBlocking {
            val actual = givenRoot1 isIsomorphicTo givenRoot2
            assertTrue(actual)
        }

    @Test
    fun `test isIsoStructural positive`() =
        runBlocking {
            val actual = givenRoot1 isIsoStructuralTo givenRoot2
            assertTrue(actual)
        }

    @Test
    fun `test isIsomorphic negative`() =
        runBlocking {
            val actual = givenRoot1 isIsomorphicTo givenRoot2.getChildren().first()
            assertFalse(actual)
        }

    @Test
    fun `test isIsoStructural negative`() =
        runBlocking {
            val actual = givenRoot1 isIsoStructuralTo givenRoot2.getChildren().first()
            assertFalse(actual)
        }

    @Test
    fun `test isIsomorphic negative with different text`() =
        runBlocking {
            val diffLabelChild1 = gumTree("same", "different1")
            val diffLabelChild2 = gumTree("same", "different2")
            givenRoot1.addChild(diffLabelChild1)
            givenRoot2.addChild(diffLabelChild2)
            val actual = givenRoot1 isIsomorphicTo givenRoot2
            assertFalse(actual)
        }

    @Test
    fun `test isIsoStructural positive with different label`() =
        runBlocking {
            val diffLabelChild1 = gumTree("same", "different1")
            val diffLabelChild2 = gumTree("same", "different1")
            givenRoot1.addChild(diffLabelChild1)
            givenRoot2.addChild(diffLabelChild2)
            val actual = givenRoot1 isIsoStructuralTo givenRoot2
            assertTrue(actual)
        }

    @Test
    fun `test isIsomorphic should have same hashMemo`() =
        runBlocking {
            val hashMemo1 = createHashMemoOf(givenRoot1)
            val hashMemo2 = createHashMemoOf(givenRoot2)
            assertEquals(hashMemo1[givenRoot1.id], hashMemo2[givenRoot2.id])
        }
}