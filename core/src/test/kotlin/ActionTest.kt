import helpers.gumTree
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import tw.xcc.gumtree.model.operations.SingleDeleteAction
import tw.xcc.gumtree.model.operations.SingleInsertAction
import tw.xcc.gumtree.model.operations.SingleUpdateAction
import tw.xcc.gumtree.model.operations.TreeDeleteAction
import tw.xcc.gumtree.model.operations.TreeInsertAction
import tw.xcc.gumtree.model.operations.TreeMoveAction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Execution(ExecutionMode.CONCURRENT)
class ActionTest {
    private lateinit var givenParent: GumTree
    private lateinit var givenNode: GumTree
    private val givenGrammarName = "good morning"
    private val givenText = "Hi there"

    @BeforeTest
    fun setUp() {
        givenParent =
            gumTree(givenGrammarName, givenText) {
                child(givenGrammarName, givenText)
            }
        givenNode = givenParent.getChildren().first()
    }

    @Test
    fun `test content of SingleDeleteAction`() {
        val action = SingleDeleteAction(givenNode)

        assertEquals("DELETE", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.oldInfo
        )
        assertEquals(null, action.newInfo)
    }

    @Test
    fun `test content of SingleInsertAction`() {
        val action = SingleInsertAction(givenNode, givenParent, 3)

        assertEquals("INSERT", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(null, action.oldInfo)
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.newInfo
        )
    }

    @Test
    fun `test content of SingleUpdateAction`() {
        val givenOldType = TreeType.empty()
        val givenOldText = "old text"
        val action = SingleUpdateAction(givenNode, givenOldType, givenOldText)

        assertEquals("UPDATE", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(
            GumTree.Info.of(type = givenOldType, text = givenOldText),
            action.oldInfo
        )
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.newInfo
        )
    }

    @Test
    fun `test content of TreeDeleteAction`() {
        val action = TreeDeleteAction(givenNode)

        assertEquals("TREE-DELETE", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.oldInfo
        )
        assertEquals(null, action.newInfo)
    }

    @Test
    fun `test content of TreeInsertAction`() {
        val action = TreeInsertAction(givenNode, givenParent, 3)

        assertEquals("TREE-INSERT", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(null, action.oldInfo)
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.newInfo
        )
    }

    @Test
    fun `test content of TreeMoveAction`() {
        val givenNewLine = 5
        val givenNewPosOfLine = 6
        val action = TreeMoveAction(givenNode, givenParent, 3, givenNewLine, givenNewPosOfLine)

        assertEquals("TREE-MOVE", action.name)
        assertEquals(givenNode, action.node)
        assertEquals(
            GumTree.Info.of(type = TreeType(givenGrammarName), text = givenText),
            action.oldInfo
        )
        assertEquals(
            GumTree.Info.of(
                type = TreeType(givenGrammarName),
                text = givenText,
                line = givenNewLine,
                posOfLine = givenNewPosOfLine
            ),
            action.newInfo
        )
    }
}