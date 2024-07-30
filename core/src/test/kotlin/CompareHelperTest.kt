import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.createHashMemoOf
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
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
        givenRoot1 = GumTree(TreeType("same"))
        givenRoot2 = GumTree(TreeType("same"))

        listOf(givenRoot1, givenRoot2).forEach { root ->
            val l1Child1 = GumTree(TreeType("same"))
            val l1Child2 = GumTree(TreeType("same"))
            val l1Child3 = GumTree(TreeType("same"))
            val l1Child4 = GumTree(TreeType("same"))

            val l2Child1 = GumTree(TreeType("same"))
            val l2Child2 = GumTree(TreeType("same"))
            val l2Child3 = GumTree(TreeType("same"))
            val l2Child4 = GumTree(TreeType("same"))
            val l2Child5 = GumTree(TreeType("same"))

            val l3Child1 = GumTree(TreeType("same"))
            val l3Child2 = GumTree(TreeType("same"))
            val l3Child3 = GumTree(TreeType("same"))

            root.setChildrenTo(listOf(l1Child1, l1Child2, l1Child3, l1Child4))
            l1Child1.setChildrenTo(listOf(l2Child1, l2Child2))
            l1Child2.setChildrenTo(listOf(l2Child3))
            l1Child3.setChildrenTo(listOf(l2Child4, l2Child5))
            l2Child2.setChildrenTo(listOf(l3Child1, l3Child2))
            l2Child4.setChildrenTo(listOf(l3Child3))
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
    fun `test isIsomorphic negative with different label`() =
        runBlocking {
            val diffLabelChild1 = GumTree(TreeType("same"), "different1")
            val diffLabelChild2 = GumTree(TreeType("same"), "different2")
            givenRoot1.addChild(diffLabelChild1)
            givenRoot2.addChild(diffLabelChild2)
            val actual = givenRoot1 isIsomorphicTo givenRoot2
            assertFalse(actual)
        }

    @Test
    fun `test isIsoStructural positive with different label`() =
        runBlocking {
            val diffLabelChild1 = GumTree(TreeType("same"), "different1")
            val diffLabelChild2 = GumTree(TreeType("same"), "different2")
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