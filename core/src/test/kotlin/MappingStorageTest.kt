import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import tw.xcc.gumtree.model.TreeType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class MappingStorageTest {
    private lateinit var givenStorage: MappingStorage

    @BeforeTest
    fun setUp() {
        givenStorage = MappingStorage()
    }

    @Test
    fun `test size`() {
        val actualSize = givenStorage.size
        assertEquals(0, actualSize)
    }

    @Test
    fun `test addMapping`() {
        repeat(1000) {
            val left = GumTree(TreeType("left"))
            val right = GumTree(TreeType("right"))
            givenStorage.addMappingOf(left to right)
        }
        val actualSize = givenStorage.size
        assertEquals(1000, actualSize)
    }

    @Test
    fun `test addMappingRecursively`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        left.setChildrenTo(listOf(GumTree(TreeType("left")), GumTree(TreeType("left"))))
        right.setChildrenTo(listOf(GumTree(TreeType("right")), GumTree(TreeType("right"))))
        givenStorage.addMappingRecursivelyOf(left to right)
        val actualSize = givenStorage.size
        assertEquals(3, actualSize)
    }

    @Test
    fun `test addMappingRecursively without symmetric`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        left.setChildrenTo(listOf(GumTree(TreeType("left")), GumTree(TreeType("left"))))
        right.setChildrenTo(listOf(GumTree(TreeType("right"))))
        givenStorage.addMappingRecursivelyOf(left to right)
        val actualSize = givenStorage.size
        assertEquals(2, actualSize)
    }

    @Test
    fun `test removeMapping`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        givenStorage.removeMappingOf(left to right)
        val actualSize = givenStorage.size
        assertEquals(0, actualSize)
    }

    @Test
    fun `test getMappingOfLeft`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actualRight = givenStorage.getMappingOfLeft(left)
        assertEquals(right, actualRight)
    }

    @Test
    fun `test getMappingOfRight`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actualLeft = givenStorage.getMappingOfRight(right)
        assertEquals(left, actualLeft)
    }

    @Test
    fun `test getMappingOfLeft with non-exist`() {
        val nonAddedLeft = GumTree(TreeType("left"))
        val actualRight = givenStorage.getMappingOfLeft(nonAddedLeft)
        assertEquals(null, actualRight)
    }

    @Test
    fun `test getMappingOfRight with non-exist`() {
        val nonAddedRight = GumTree(TreeType("right"))
        val actualLeft = givenStorage.getMappingOfRight(nonAddedRight)
        assertEquals(null, actualLeft)
    }

    @Test
    fun `test getMappingOfLeft with multiple`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actualRight1 = givenStorage.getMappingOfLeft(left1)
        assertEquals(right1, actualRight1)
        val actualRight2 = givenStorage.getMappingOfLeft(left2)
        assertEquals(right2, actualRight2)
    }

    @Test
    fun `test getMappingOfRight with multiple`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actualLeft1 = givenStorage.getMappingOfRight(right1)
        assertEquals(left1, actualLeft1)
        val actualLeft2 = givenStorage.getMappingOfRight(right2)
        assertEquals(left2, actualLeft2)
    }

    @Test
    fun `test isLeftMapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.isLeftMapped(left)
        assertTrue(actual)
    }

    @Test
    fun `test isLeftMapped with non-exist`() {
        val nonAddedLeft = GumTree(TreeType("left"))
        val actual = givenStorage.isLeftMapped(nonAddedLeft)
        assertFalse(actual)
    }

    @Test
    fun `test isLeftMapped with multiple`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actual1 = givenStorage.isLeftMapped(left1)
        assertTrue(actual1)
        val actual2 = givenStorage.isLeftMapped(left2)
        assertTrue(actual2)
    }

    @Test
    fun `test isRightMapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.isRightMapped(right)
        assertTrue(actual)
    }

    @Test
    fun `test isRightMapped with non-exist`() {
        val nonAddedRight = GumTree(TreeType("right"))
        val actual = givenStorage.isRightMapped(nonAddedRight)
        assertFalse(actual)
    }

    @Test
    fun `test isRightMapped with multiple`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actual1 = givenStorage.isRightMapped(right1)
        assertTrue(actual1)
        val actual2 = givenStorage.isRightMapped(right2)
        assertTrue(actual2)
    }

    @Test
    fun `test isAnyOfLeftsUnMapped`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        val actual = givenStorage.isAnyOfLeftsUnMapped(listOf(left1, left2))
        assertTrue(actual)
    }

    @Test
    fun `test isAnyOfLeftsUnMapped with all mapped`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actual = givenStorage.isAnyOfLeftsUnMapped(listOf(left1, left2))
        assertFalse(actual)
    }

    @Test
    fun `test isAnyOfLeftsUnMapped with all unmapped`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val actual = givenStorage.isAnyOfLeftsUnMapped(listOf(left1, left2))
        assertTrue(actual)
    }

    @Test
    fun `test isAnyOfRightsUnMapped`() {
        val left1 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        val actual = givenStorage.isAnyOfRightsUnMapped(listOf(right1, right2))
        assertTrue(actual)
    }

    @Test
    fun `test isAnyOfRightsUnMapped with all mapped`() {
        val left1 = GumTree(TreeType("left"))
        val left2 = GumTree(TreeType("left"))
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left1 to right1)
        givenStorage.addMappingOf(left2 to right2)
        val actual = givenStorage.isAnyOfRightsUnMapped(listOf(right1, right2))
        assertFalse(actual)
    }

    @Test
    fun `test isAnyOfRightsUnMapped with all unmapped`() {
        val right1 = GumTree(TreeType("right"))
        val right2 = GumTree(TreeType("right"))
        val actual = givenStorage.isAnyOfRightsUnMapped(listOf(right1, right2))
        assertTrue(actual)
    }

    @Test
    fun `test areBothUnMapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        val actual = givenStorage.areBothUnMapped(left to right)
        assertTrue(actual)
    }

    @Test
    fun `test areBothUnMapped of all mapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.areBothUnMapped(left to right)
        assertFalse(actual)
    }

    @Test
    fun `test areBothUnMapped of all unMapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.areBothUnMapped(right to left)
        assertTrue(actual)
    }

    @Test
    fun `test hasUnMappedDescendentOfLeft`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        val leftChild = GumTree(TreeType("left"))
        left.setChildrenTo(listOf(leftChild))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.hasUnMappedDescendentOfLeft(left)
        assertTrue(actual)
    }

    @Test
    fun `test hasUnMappedDescendentOfLeft with all mapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        val leftChild = GumTree(TreeType("left"))
        left.setChildrenTo(listOf(leftChild))
        givenStorage.addMappingOf(left to right)
        givenStorage.addMappingOf(leftChild to right)
        val actual = givenStorage.hasUnMappedDescendentOfLeft(left)
        assertFalse(actual)
    }

    @Test
    fun `test hasUnMappedDescendentOfRight`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        val rightChild = GumTree(TreeType("right"))
        right.setChildrenTo(listOf(rightChild))
        givenStorage.addMappingOf(left to right)
        val actual = givenStorage.hasUnMappedDescendentOfRight(right)
        assertTrue(actual)
    }

    @Test
    fun `test hasUnMappedDescendentOfRight with all mapped`() {
        val left = GumTree(TreeType("left"))
        val right = GumTree(TreeType("right"))
        val rightChild = GumTree(TreeType("right"))
        right.setChildrenTo(listOf(rightChild))
        givenStorage.addMappingOf(left to right)
        givenStorage.addMappingOf(left to rightChild)
        val actual = givenStorage.hasUnMappedDescendentOfRight(right)
        assertFalse(actual)
    }
}