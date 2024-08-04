import helpers.gumTree
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.createHashMemoOf
import tw.xcc.gumtree.model.GumTree
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class HashMemoHelperTest {
    private lateinit var givenRoot: GumTree

    private fun create10Children(lv: Int): List<GumTree> {
        val result = mutableListOf<GumTree>()
        repeat(10) { idx ->
            result.add(gumTree("lv$lv-$idx", "lv$lv-$idx"))
        }
        return result
    }

    @BeforeTest
    fun setUp() {
        givenRoot = GumTree()
        val level = 5 // will cause 111,111 nodes
        val needChildrens = mutableListOf<GumTree>()
        needChildrens.add(givenRoot)
        repeat(level) { idx ->
            val needChildren = needChildrens.toList()
            needChildrens.clear()
            needChildren.forEach { parent ->
                val children = create10Children(idx)
                parent.setChildrenTo(children)
                needChildrens.addAll(children)
            }
        }
    }

    @Test
    fun `test createHashMemo for a single node`() =
        runBlocking {
            val hashMemo = createHashMemoOf(GumTree())
            assertEquals(1, hashMemo.size)
        }

    @Test
    fun `test createHashMemo for given tree`() =
        runBlocking {
            val hashMemo = createHashMemoOf(givenRoot)
            assertEquals(111111, hashMemo.size)
        }

    @Test
    fun `test createHashMemo consistency`() =
        runBlocking {
            val hashMemo1 = createHashMemoOf(givenRoot)
            val hashMemo2 = createHashMemoOf(givenRoot)
            assertEquals(hashMemo1, hashMemo2)
        }

    @Test
    fun `test createHashMemo can really store for every node`() =
        runBlocking {
            val hashMemo = createHashMemoOf(givenRoot)
            val allDescendents = givenRoot.descendents
            val actual = allDescendents.all { hashMemo.containsKey(it.id) }
            assertTrue(actual)
        }
}