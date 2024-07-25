import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.comparator.calculateDiceValue
import tw.xcc.gumtree.matchers.comparator.hasSameParent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class SharedFunctionTest {
    @Test
    fun `test dice function`() {
        val sizeA = 298347
        val sizeB = 182764
        val theirIntersectionSize = 67323

        val expectDice = 0.2798647297609076
        val actualDice = calculateDiceValue(theirIntersectionSize, sizeA, sizeB)

        assertEquals(expectDice, actualDice, 0.0001)
    }

    @Test
    fun `test dice function with zero size`() {
        val sizeA = 0
        val sizeB = 0
        val theirIntersectionSize = 0

        val expectDice = 0.0
        val actualDice = calculateDiceValue(theirIntersectionSize, sizeA, sizeB)

        assertEquals(expectDice, actualDice, 0.0001)
    }

    @Test
    fun `test dice function with zero size and intersection`() {
        val sizeA = 0
        val sizeB = 0
        val theirIntersectionSize = 8787878

        val expectDice = 1.0
        val actualDice = calculateDiceValue(theirIntersectionSize, sizeA, sizeB)

        assertEquals(expectDice, actualDice, 0.0001)
    }

    @Test
    fun `test dice function with zero intersection`() {
        val sizeA = 298347
        val sizeB = 182764
        val theirIntersectionSize = 0

        val expectDice = 0.0
        val actualDice = calculateDiceValue(theirIntersectionSize, sizeA, sizeB)

        assertEquals(expectDice, actualDice, 0.0001)
    }

    @Test
    fun `test has same parent`() {
        val parent = FakeTinnyTree()
        val child1 = FakeTinnyTree().also { it.setParentTo(parent) }
        val child2 = FakeTinnyTree().also { it.setParentTo(parent) }
        val child3 = FakeTinnyTree().also { it.setParentTo(parent) }
        val child4 = FakeTinnyTree().also { it.setParentTo(parent) }

        val m1 = child1 to child2
        val m2 = child3 to child4

        val actualResult = hasSameParent(m1, m2)

        assertTrue(actualResult)
    }
}