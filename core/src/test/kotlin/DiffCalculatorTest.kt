import kotlinx.coroutines.runBlocking
import shared.realTree1
import tw.xcc.gumtree.DiffCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DiffCalculatorTest {
    @Test
    fun `test real tree`() {
        val (tree1, tree2) = realTree1
        val calculator = DiffCalculator()

        runBlocking {
            val editScript = calculator.computeEditScriptFrom(tree1 to tree2)
            assertEquals(7, editScript.size)
        }
    }
}