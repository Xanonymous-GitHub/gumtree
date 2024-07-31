import helpers.gumTree
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun `test copy constructor`() {
        val sourceTree =
            gumTree("root", "label", 123, 456) {
                child("child-layer1", "child1", 12301, 45601) {
                    child("child-layer2", "child2", 12302, 45602)
                }
                child("child-layer1", "child3", 12303, 45603)
                child("child-layer1", "child4", 12304, 45604) {
                    child("child-layer2", "child5", 12305, 45605)
                    child("child-layer2", "child6", 12306, 45606)
                }
            }
        val copiedTree = GumTree(sourceTree)
        assertEquals(sourceTree.type, copiedTree.type)
        assertEquals(sourceTree.label, copiedTree.label)
        assertEquals(sourceTree.pos, copiedTree.pos)
        assertEquals(sourceTree.length, copiedTree.length)
        assertEquals(sourceTree.id, copiedTree.id)
        assertEquals(sourceTree.descendents.size, copiedTree.descendents.size)
        assertEquals(sourceTree.ancestors.size, copiedTree.ancestors.size)
        assertEquals(sourceTree.height, copiedTree.height)
        assertEquals(sourceTree.depth, copiedTree.depth)
        assertEquals(sourceTree.childCount(), copiedTree.childCount())

        for (i in 0 until sourceTree.childCount()) {
            val sourceChild = sourceTree.childAt(i)
            val copiedChild = copiedTree.childAt(i)
            assertEquals(sourceChild?.type, copiedChild?.type)
            assertEquals(sourceChild?.label, copiedChild?.label)
            assertEquals(sourceChild?.pos, copiedChild?.pos)
            assertEquals(sourceChild?.length, copiedChild?.length)
            assertEquals(sourceChild?.id, copiedChild?.id)
            assertEquals(sourceChild?.descendents?.size, copiedChild?.descendents?.size)
            assertEquals(sourceChild?.ancestors?.size, copiedChild?.ancestors?.size)
            assertEquals(sourceChild?.height, copiedChild?.height)
            assertEquals(sourceChild?.depth, copiedChild?.depth)
            assertEquals(sourceChild?.childCount(), copiedChild?.childCount())
        }
    }

    @Test
    fun `test tree pos`() {
        val actualTreePos = givenTree.pos
        assertEquals(-1, actualTreePos)
        val givenNewTreePos = 9487
        val treeWithCustomPos = GumTree(TreeType.empty(), "label", givenNewTreePos)
        val newActualTreePos = treeWithCustomPos.pos
        assertEquals(givenNewTreePos, newActualTreePos)
    }

    @Test
    fun `test tree length`() {
        val actualTreeLength = givenTree.length
        assertEquals(-1, actualTreeLength)
        val givenNewTreeLength = 9487
        val treeWithCustomLength = GumTree(TreeType.empty(), "label", -1, givenNewTreeLength)
        val newActualTreeLength = treeWithCustomLength.length
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

    @Test
    fun `test positionOfParent`() {
        val givenPositionOfParent = 9487
        val parent = GumTree(TreeType("parentType"))
        parent.insertChildAt(givenPositionOfParent, givenTree)
        val actualPositionOfParent = givenTree.positionOfParent
        assertEquals(givenPositionOfParent, actualPositionOfParent)
    }

    @Test
    fun `test similarityProperties`() {
        val actualSimilarityProperties = givenTree.similarityProperties()
        val expectedSimilarityProperties = "<$givenTreeLabel>[$givenTreeType]<"
        assertEquals(expectedSimilarityProperties, actualSimilarityProperties)
    }

    @Test
    fun `test toNewFrozen`() {
        val child = GumTree(TreeType.empty(), "child")
        givenTree.addChild(child)
        val frozen = givenTree.toNewFrozen()
        assertEquals(givenTree.id, frozen.id)
        assertTrue(frozen !== givenTree)
        assertEquals(child.id, frozen.childAt(0)?.id)
        assertTrue(child !== frozen.childAt(0))
    }

    @Test
    fun `test tryRemoveChild`() {
        val child = GumTree(TreeType.empty(), "child")
        givenTree.addChild(child)
        val actualResult = givenTree.tryRemoveChild(child)
        assertTrue(actualResult)
        assertEquals(0, givenTree.childCount())
    }

    @Test
    fun `test tryRemoveChild with non-child`() {
        val child = GumTree(TreeType.empty(), "child")
        val actualResult = givenTree.tryRemoveChild(child)
        assertFalse(actualResult)
    }
}