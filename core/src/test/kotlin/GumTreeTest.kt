import helpers.gumTree
import org.junit.jupiter.api.assertThrows
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
    private val givenTreeText = "testLabel"
    private lateinit var givenTree: GumTree

    @BeforeTest
    fun setUp() {
        givenTree =
            GumTree(
                GumTree.Info.of(
                    type = givenTreeType,
                    text = givenTreeText
                )
            )
    }

    @Test
    fun `test default constructor`() {
        val actualTreeType = givenTree.info.type
        val actualTreeLabel = givenTree.info.text
        assertEquals(givenTreeType, actualTreeType, "TreeType should be equal")
        assertEquals(givenTreeText, actualTreeLabel, "TreeLabel should be equal")
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
        assertEquals(sourceTree.info, copiedTree.info)
        assertEquals(sourceTree.id, copiedTree.id)
        assertEquals(sourceTree.descendents.size, copiedTree.descendents.size)
        assertEquals(sourceTree.ancestors.size, copiedTree.ancestors.size)
        assertEquals(sourceTree.height, copiedTree.height)
        assertEquals(sourceTree.depth, copiedTree.depth)
        assertEquals(sourceTree.childCount(), copiedTree.childCount())

        for (i in 0 until sourceTree.childCount()) {
            val sourceChild = sourceTree.childAt(i)
            val copiedChild = copiedTree.childAt(i)
            assertEquals(sourceChild?.info, copiedChild?.info)
            assertEquals(sourceChild?.id, copiedChild?.id)
            assertEquals(sourceChild?.descendents?.size, copiedChild?.descendents?.size)
            assertEquals(sourceChild?.ancestors?.size, copiedChild?.ancestors?.size)
            assertEquals(sourceChild?.height, copiedChild?.height)
            assertEquals(sourceChild?.depth, copiedChild?.depth)
            assertEquals(sourceChild?.childCount(), copiedChild?.childCount())
        }
    }

    @Test
    fun `test tree posOfLine`() {
        val actualTreePos = givenTree.info.posOfLine
        assertEquals(-1, actualTreePos)
        val givenNewTreePos = 9487
        val treeWithCustomPos = GumTree(GumTree.Info.of(posOfLine = givenNewTreePos))
        val newActualTreePos = treeWithCustomPos.info.posOfLine
        assertEquals(givenNewTreePos, newActualTreePos)
    }

    @Test
    fun `test tree text length`() {
        val actualTreeLength = givenTree.info.text.length
        assertEquals(9, actualTreeLength)
        val givenNewTreeLength = 7
        val treeWithCustomLength = GumTree(GumTree.Info.of(text = "1234567"))
        val newActualTreeLength = treeWithCustomLength.info.text.length
        assertEquals(givenNewTreeLength, newActualTreeLength)
    }

    @Test
    fun `test insertChildAt`() {
        val childTreeType = TreeType("childType")
        val childTreeLabel = "childLabel"

        for (insertPos in 0..5) {
            val childTree =
                GumTree(
                    GumTree.Info.of(
                        type = childTreeType,
                        text = childTreeLabel
                    )
                )
            givenTree.insertChildAt(insertPos, childTree)
            val actualChildTree = givenTree.childAt(insertPos)
            assertEquals(childTree, actualChildTree, "Child tree should be equal")
        }

        val childTree2 =
            GumTree(
                GumTree.Info.of(
                    type = childTreeType,
                    text = childTreeLabel
                )
            )
        givenTree.insertChildAt(3, childTree2)
        val actualChildTree2 = givenTree.childAt(3)
        assertEquals(childTree2, actualChildTree2, "Child tree should be equal")

        assertThrows<IndexOutOfBoundsException> {
            givenTree.insertChildAt(878787878, childTree2)
        }
    }

    @Test
    fun `test hasSameTypeAs`() {
        val otherTreeType = TreeType("otherType")
        val otherTree =
            GumTree(
                GumTree.Info.of(
                    type = otherTreeType,
                    text = givenTreeText
                )
            )
        val actualResult = givenTree hasSameTypeAs otherTree
        assertFalse(actualResult, "TreeType should not be equal")
    }

    @Test
    fun `test hasSameTextAs`() {
        val otherTreeText = "otherLabel"
        val otherTree =
            GumTree(
                GumTree.Info.of(
                    type = givenTreeType,
                    text = otherTreeText
                )
            )
        val actualResult = givenTree hasSameTextAs otherTree
        assertFalse(actualResult, "TreeLabel should not be equal")
    }

    @Test
    fun `test positionOfParent`() {
        val parent = GumTree()

        for (givenPositionOfParent in 0..3) {
            parent.insertChildAt(givenPositionOfParent, givenTree)
            val actualPositionOfParent = givenTree.positionOfParent
            assertEquals(givenPositionOfParent, actualPositionOfParent)
        }
    }

    @Test
    fun `test similarityProperties`() {
        val actualSimilarityProperties = givenTree.similarityProperties()
        val expectedSimilarityProperties = "<$givenTreeText>[$givenTreeType]<"
        assertEquals(expectedSimilarityProperties, actualSimilarityProperties)
    }

    @Test
    fun `test toNewFrozen`() {
        val child = GumTree()
        givenTree.addChild(child)
        val frozen = givenTree.toNewFrozen()
        assertEquals(givenTree.id, frozen.id)
        assertTrue(frozen !== givenTree)
        assertEquals(child.id, frozen.childAt(0)?.id)
        assertTrue(child !== frozen.childAt(0))
    }

    @Test
    fun `test tryRemoveChild`() {
        val child = GumTree()
        givenTree.addChild(child)
        val actualResult = givenTree.tryRemoveChild(child)
        assertTrue(actualResult)
        assertEquals(0, givenTree.childCount())
    }

    @Test
    fun `test tryRemoveChild with non-child`() {
        val child = GumTree()
        val actualResult = givenTree.tryRemoveChild(child)
        assertFalse(actualResult)
    }

    @Test
    fun `test leaveParent`() {
        val parent = GumTree()
        parent.addChild(givenTree)
        val actualResult = givenTree.leaveParent()
        assertTrue(actualResult)
        assertEquals(0, parent.childCount())
    }

    @Test
    fun `test leaveParent with no parent`() {
        val actualResult = givenTree.leaveParent()
        assertFalse(actualResult)
    }
}