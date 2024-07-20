import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.BasicTree
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class BasicTreeTest {
    private lateinit var givenTree: BasicTree<TestBasicTree>

    @BeforeTest
    fun setUp() {
        givenTree = TestBasicTree()
    }

    @Test
    fun `test default parent`() {
        val actualParent = givenTree.getParent()
        assertEquals(null, actualParent)
    }

    @Test
    fun `test default children`() {
        val actualChildren = givenTree.getChildren()
        assertContentEquals(emptyList(), actualChildren)
    }

    @Test
    fun `test default descendents`() {
        val actualDescendents = givenTree.descendents
        assertContentEquals(emptyList(), actualDescendents)
    }

    @Test
    fun `test addChild`() {
        val child1 = TestBasicTree()
        val child2 = TestBasicTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        val actualChildren = givenTree.getChildren()
        assertContentEquals(listOf(child1, child2), actualChildren)
    }

    @Test
    fun `test setChildrenTo`() {
        val child1 = TestBasicTree()
        val child2 = TestBasicTree()
        givenTree.setChildrenTo(listOf(child1, child2))
        val actualChildren = givenTree.getChildren()
        assertContentEquals(listOf(child1, child2), actualChildren)
    }

    @Test
    fun `test setParentTo`() {
        val parent = TestBasicTree()
        givenTree.setParentTo(parent)
        val actualParent = givenTree.getParent()
        assertEquals(parent, actualParent)
    }

    @Test
    fun `test childAt`() {
        val child1 = TestBasicTree()
        val child2 = TestBasicTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        val actualChild1 = givenTree.childAt(0)
        val actualChild2 = givenTree.childAt(1)
        assertEquals(child1, actualChild1)
        assertEquals(child2, actualChild2)
    }

    @Test
    fun `test childCount`() {
        val child1 = TestBasicTree()
        val child2 = TestBasicTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        val actualChildCount = givenTree.childCount()
        assertEquals(2, actualChildCount)
    }

    @Test
    fun `test default isRoot`() {
        val actualIsRoot = givenTree.isRoot()
        assertTrue(actualIsRoot)
    }

    @Test
    fun `test isRoot`() {
        val parent = TestBasicTree()
        givenTree.setParentTo(parent)
        val actualIsRoot = givenTree.isRoot()
        assertFalse(actualIsRoot)
    }

    @Test
    fun `test default isLeaf`() {
        val actualIsLeaf = givenTree.isLeaf()
        assertTrue(actualIsLeaf)
    }

    @Test
    fun `test isLeaf`() {
        val child = TestBasicTree()
        givenTree.addChild(child)
        val actualIsLeaf = givenTree.isLeaf()
        assertFalse(actualIsLeaf)
    }

    private class TestBasicTree : BasicTree<TestBasicTree>() {
        override val self: TestBasicTree
            get() = this

        override fun similarityHashCode(): Int = throw NotImplementedError()

        override fun similarityStructureHashCode(): Int = throw NotImplementedError()
    }
}