import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.bfsOrderOf
import tw.xcc.gumtree.helper.postOrderOf
import tw.xcc.gumtree.helper.preOrderOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

@Execution(ExecutionMode.CONCURRENT)
internal class TraversalHelperTest {
    private lateinit var givenRoot: FakeTinnyTree

    @BeforeTest
    fun setUp() {
        givenRoot = FakeTinnyTree()
    }

    @Test
    fun `test preOrdered`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        val child3 = FakeTinnyTree()
        val child4 = FakeTinnyTree()

        givenRoot.addChild(child1)
        child1.addChild(child2)
        child1.addChild(child3)
        givenRoot.addChild(child4)

        val actualPreOrdered = mutableListOf<FakeTinnyTree>()
        preOrderOf(givenRoot) { actualPreOrdered.add(it) }

        val expectedPreOrdered = listOf(givenRoot, child1, child2, child3, child4)
        assertContentEquals(expectedPreOrdered, actualPreOrdered)
    }

    @Test
    fun `test preOrdered with empty tree`() {
        val actualPreOrdered = mutableListOf<FakeTinnyTree>()
        preOrderOf(givenRoot) { actualPreOrdered.add(it) }

        val expectedPreOrdered = listOf(givenRoot)
        assertContentEquals(expectedPreOrdered, actualPreOrdered)
    }

    @Test
    fun `test preOrdered with single child`() {
        val child1 = FakeTinnyTree()
        givenRoot.addChild(child1)

        val actualPreOrdered = mutableListOf<FakeTinnyTree>()
        preOrderOf(givenRoot) { actualPreOrdered.add(it) }

        val expectedPreOrdered = listOf(givenRoot, child1)
        assertContentEquals(expectedPreOrdered, actualPreOrdered)
    }

    @Test
    fun `test postOrdered`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        val child3 = FakeTinnyTree()
        val child4 = FakeTinnyTree()

        givenRoot.addChild(child1)
        child1.addChild(child2)
        child1.addChild(child3)
        givenRoot.addChild(child4)

        val actualPostOrdered = mutableListOf<FakeTinnyTree>()
        postOrderOf(givenRoot) { actualPostOrdered.add(it) }

        val expectedPostOrdered = listOf(child2, child3, child1, child4, givenRoot)
        assertContentEquals(expectedPostOrdered, actualPostOrdered)
    }

    @Test
    fun `test postOrdered with empty tree`() {
        val actualPostOrdered = mutableListOf<FakeTinnyTree>()
        postOrderOf(givenRoot) { actualPostOrdered.add(it) }

        val expectedPostOrdered = listOf(givenRoot)
        assertContentEquals(expectedPostOrdered, actualPostOrdered)
    }

    @Test
    fun `test postOrdered with single child`() {
        val child1 = FakeTinnyTree()
        givenRoot.addChild(child1)

        val actualPostOrdered = mutableListOf<FakeTinnyTree>()
        postOrderOf(givenRoot) { actualPostOrdered.add(it) }

        val expectedPostOrdered = listOf(child1, givenRoot)
        assertContentEquals(expectedPostOrdered, actualPostOrdered)
    }

    @Test
    fun `test bfsOrdered`() {
        val child1 = FakeTinnyTree()
        val child2 = FakeTinnyTree()
        val child3 = FakeTinnyTree()
        val child4 = FakeTinnyTree()

        givenRoot.addChild(child1)
        child1.addChild(child2)
        child1.addChild(child3)
        givenRoot.addChild(child4)

        val actualBfsOrdered = mutableListOf<FakeTinnyTree>()
        bfsOrderOf(givenRoot) { actualBfsOrdered.add(it) }

        val expectedBfsOrdered = listOf(givenRoot, child1, child4, child2, child3)
        assertContentEquals(expectedBfsOrdered, actualBfsOrdered)
    }
}