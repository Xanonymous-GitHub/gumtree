import helpers.gumTree
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.GumTreeView
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@Execution(ExecutionMode.CONCURRENT)
class GumTreeViewTest {
    private lateinit var originTree: GumTree
    private lateinit var treeView: GumTreeView

    @BeforeTest
    fun setUp() {
        val root =
            gumTree("1", "label") {
                child("4", "label") {
                    child("5", "label")
                    child("6", "label")
                }
                child("4", "label") {
                    child("5", "label")
                    child("6", "label")
                }
                child("3", "label")
                child("7", "label") {
                    child("8", "label")
                    child("9", "label")
                }
            }

        originTree = root.getChildren()[1]
        treeView =
            GumTreeView
                .frozeEntireTreeFrom(originTree)
                .getChildren()[1] as GumTreeView
    }

    @Test
    fun `test can get descendents`() {
        assertContentEquals(originTree.descendents, treeView.descendents)
    }

    @Test
    fun `test can get ancestors`() {
        assertContentEquals(originTree.ancestors, treeView.ancestors)
    }

    @Test
    fun `test same subTreeSize`() {
        assertEquals(originTree.subTreeSize, treeView.subTreeSize)
    }

    @Test
    fun `test same height`() {
        assertEquals(originTree.height, treeView.height)
    }

    @Test
    fun `test same depth`() {
        assertEquals(originTree.depth, treeView.depth)
    }

    @Test
    fun `test same positionOfParent`() {
        assertEquals(originTree.positionOfParent, treeView.positionOfParent)
    }

    @Test
    fun `test same info`() {
        assertEquals(originTree.info, treeView.info)
    }

    @Test
    fun `test can not change info`() {
        assertThrows<NoSuchMethodException> {
            treeView.info = GumTree.Info.empty()
        }
    }

    @Test
    fun `test same postOrdered`() {
        assertContentEquals(originTree.postOrdered(), treeView.postOrdered())
    }

    @Test
    fun `test same preOrdered`() {
        assertContentEquals(originTree.preOrdered(), treeView.preOrdered())
    }

    @Test
    fun `test toNewFrozen is actually return the same thing`() {
        assertEquals(treeView.toNewFrozen(), treeView)
    }

    @Test
    fun `test can not set children`() {
        assertThrows<NoSuchMethodException> {
            treeView.setChildrenTo(listOf())
        }
    }

    @Test
    fun `test can not set parent`() {
        assertThrows<NoSuchMethodException> {
            treeView.setParentTo(null)
        }
    }

    @Test
    fun `test can not insert child`() {
        assertThrows<NoSuchMethodException> {
            treeView.insertChildAt(0, gumTree("1", "label"))
        }
    }

    @Test
    fun `test can not add child`() {
        assertThrows<NoSuchMethodException> {
            treeView.addChild(gumTree("1", "label"))
        }
    }

    @Test
    fun `test can not remove child`() {
        assertThrows<NoSuchMethodException> {
            treeView.tryRemoveChild(gumTree("1", "label"))
        }
    }

    @Test
    fun `test can not leave parent`() {
        assertThrows<NoSuchMethodException> {
            treeView.leaveParent()
        }
    }
}