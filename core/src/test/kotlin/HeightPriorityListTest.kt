import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.HeightPriorityList
import tw.xcc.gumtree.model.TreeType
import java.util.NoSuchElementException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class HeightPriorityListTest {
    private lateinit var givenList: HeightPriorityList
    private lateinit var node0: GumTree
    private lateinit var node1: GumTree
    private lateinit var node2: GumTree
    private lateinit var node3: GumTree
    private lateinit var node4: GumTree
    private lateinit var node5: GumTree

    @BeforeTest
    fun setUp() {
        givenList = HeightPriorityList()

        node5 = GumTree(TreeType("node5"))
        node4 =
            GumTree(TreeType("node4"))
                .also { it.addChild(node5) }
        node3 = GumTree(TreeType("node3"))
        node2 = GumTree(TreeType("node2"))
        node1 =
            GumTree(TreeType("node1"))
                .also { it.setChildrenTo(listOf(node3, node4)) }
        node0 =
            GumTree(TreeType("node0"))
                .also { it.setChildrenTo(listOf(node1, node2)) }
    }

    @Test
    fun `test push`() {
        givenList.push(node0)
        givenList.push(node1)
        givenList.push(node2)
        givenList.push(node3)
        givenList.push(node4)
        givenList.push(node5)
        val actual = givenList.peekMax()
        val expected = 3
        assertEquals(expected, actual)
    }

    @Test
    fun `test open`() {
        givenList.open(node0)
        val actual = givenList.peekMax()
        val expected = 2
        assertEquals(expected, actual)
    }

    @Test
    fun `test pop`() {
        givenList.push(node0)
        givenList.push(node1)
        givenList.push(node2)
        givenList.push(node3)
        givenList.push(node4)
        givenList.push(node5)
        val actual = givenList.pop()
        val expected = listOf(node0)
        assertEquals(expected, actual)
    }

    @Test
    fun `test popOpen`() {
        givenList.push(node0)
        val actual = givenList.popOpen()
        val expected = listOf(node0)
        assertEquals(expected, actual)
        val actual2 = givenList.peekMax()
        val expected2 = 2
        assertEquals(expected2, actual2)
    }

    @Test
    fun `test isEmpty`() {
        val actual = givenList.isEmpty()
        assertTrue(actual)
        givenList.push(node0)
        val actual2 = givenList.isEmpty()
        assertFalse(actual2)
    }

    @Test
    fun `test clear`() {
        givenList.push(node0)
        givenList.clear()
        val actual = givenList.isEmpty()
        assertTrue(actual)
    }

    @Test
    fun `test peekMax with empty list`() {
        assertThrows<NoSuchElementException> {
            givenList.peekMax()
        }
    }

    @Test
    fun `test hasBeenSynchronizedTo`() {
        val anotherList = HeightPriorityList()
        assertFalse(givenList hasBeenSynchronizedTo anotherList)

        givenList.push(node5)
        assertFalse(givenList hasBeenSynchronizedTo anotherList)

        givenList.push(node1)
        anotherList.push(node1)
        assertTrue(givenList hasBeenSynchronizedTo anotherList)

        givenList.push(node0)
        assertTrue(givenList hasBeenSynchronizedTo anotherList)
    }

    @Test
    fun `test minHeight can avoid pushing`() {
        givenList.push(node5)
        assertThrows<NoSuchElementException> {
            givenList.pop()
        }
    }
}