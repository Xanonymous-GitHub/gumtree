import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Execution(ExecutionMode.CONCURRENT)
internal class GumTreeTest {
    private val givenTreeType = TreeType("testType")
    private val givenTreeLabel = "testLabel"
    private lateinit var givenTree: GumTree

    @BeforeTest
    fun setUp() {
        givenTree = GumTree(givenTreeType, givenTreeLabel)
    }

    @Test
    fun `test default constructor`() {
        val actualTreeType = givenTree.type
        val actualTreeLabel = givenTree.label
        assertEquals(givenTreeType, actualTreeType, "TreeType should be equal")
        assertEquals(givenTreeLabel, actualTreeLabel, "TreeLabel should be equal")
    }

    @Test
    fun `test tree pos`() {
        val actualTreePos = givenTree.pos
        assertEquals(-1, actualTreePos)
        val givenNewTreePos = 9487
        givenTree.pos = givenNewTreePos
        val newActualTreePos = givenTree.pos
        assertEquals(givenNewTreePos, newActualTreePos)
    }

    @Test
    fun `test tree length`() {
        val actualTreeLength = givenTree.length
        assertEquals(-1, actualTreeLength)
        val givenNewTreeLength = 9487
        givenTree.length = givenNewTreeLength
        val newActualTreeLength = givenTree.length
        assertEquals(givenNewTreeLength, newActualTreeLength)
    }

    @Test
    fun `test insertChildAt`() {
        val childTreeType = TreeType("childType")
        val childTreeLabel = "childLabel"
        val childTree = GumTree(childTreeType, childTreeLabel)
        val givenInsertPos = 9487
        givenTree.insertChildAt(givenInsertPos, childTree)
        val actualChildTree = givenTree.childAt(givenInsertPos)
        assertEquals(childTree, actualChildTree, "Child tree should be equal")
    }

    @Test
    fun `test hasSameTypeAs`() {
        val otherTreeType = TreeType("otherType")
        val otherTree = GumTree(otherTreeType, givenTreeLabel)
        val actualResult = givenTree hasSameTypeAs otherTree
        assertFalse(actualResult, "TreeType should not be equal")
    }

    @Test
    fun `test hasSameLabelAs`() {
        val otherTreeLabel = "otherLabel"
        val otherTree = GumTree(givenTreeType, otherTreeLabel)
        val actualResult = givenTree hasSameLabelAs otherTree
        assertFalse(actualResult, "TreeLabel should not be equal")
    }
}