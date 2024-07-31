import helpers.gumTree
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.BasicTree
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class BasicTreeTest {
    private lateinit var givenTree: BasicTree<FakeTinnyTree>

    @BeforeTest
    fun setUp() {
        givenTree = FakeTinnyTree()
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
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        child1.addChild(child2)
        givenTree.addChild(child1)
        val actualDescendentsAfterAdd = givenTree.descendents
        assertContentEquals(listOf(child1, child2), actualDescendentsAfterAdd)
    }

    @Test
    fun `test addChild`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        val actualChildren = givenTree.getChildren()
        assertContentEquals(listOf(child1, child2), actualChildren)
    }

    @Test
    fun `test setChildrenTo`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        givenTree.setChildrenTo(listOf(child1, child2))
        val actualChildren = givenTree.getChildren()
        assertContentEquals(listOf(child1, child2), actualChildren)
    }

    @Test
    fun `test setParentTo`() {
        val parent = FakeTinnyTree()
        givenTree.setParentTo(parent)
        val actualParent = givenTree.getParent()
        assertEquals(parent, actualParent)
    }

    @Test
    fun `test childAt`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        val actualChild1 = givenTree.childAt(0)
        val actualChild2 = givenTree.childAt(1)
        assertEquals(child1, actualChild1)
        assertEquals(child2, actualChild2)
    }

    @Test
    fun `test childCount`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
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
        val parent = FakeTinnyTree()
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
        val child = FakeTinnyTree()
        givenTree.addChild(child)
        val actualIsLeaf = givenTree.isLeaf()
        assertFalse(actualIsLeaf)
    }

    @Test
    fun `test height`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        val child3 = FakeTinnyTree()
        val child4 = FakeTinnyTree()
        givenTree.addChild(child1)
        givenTree.addChild(child2)
        child1.addChild(child3)
        child1.addChild(child4)
        val actualHeight = givenTree.height
        assertEquals(2, actualHeight)
    }

    @Test
    fun `test depth`() {
        val parent = FakeTinnyTree()
        val child = FakeTinnyTree()
        parent.addChild(child)
        val actualDepth = child.depth
        assertEquals(1, actualDepth)
    }

    @Test
    fun `test tree id`() {
        val actualTreeId = givenTree.id
        assertDoesNotThrow { UUID.fromString(actualTreeId) }
    }

    @Test
    fun `test ancestor finding`() {
        val tree =
            gumTree("0") {
                child("1") {
                    child("2") {
                        child("3")
                    }
                }
                child("notAncestor")
            }

        val mostDescendent = tree.descendents.find { it.type.name == "3" }
        assertNotNull(mostDescendent)

        val itsAncestors = mostDescendent.ancestors
        val ancestorNames = itsAncestors.map { it.type.name }

        assertContentEquals(listOf("2", "1", "0"), ancestorNames)

        val newRoot =
            gumTree("newRoot") {
                child("newChild")
            }
        tree.setParentTo(newRoot)

        val newAncestors = mostDescendent.ancestors
        val newAncestorNames = newAncestors.map { it.type.name }

        assertContentEquals(listOf("2", "1", "0", "newRoot"), newAncestorNames)
    }
}