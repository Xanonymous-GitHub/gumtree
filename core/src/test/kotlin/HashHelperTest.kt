import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.helper.fixedSeedHashOf
import kotlin.test.Test
import kotlin.test.assertEquals

@Execution(ExecutionMode.CONCURRENT)
internal class HashHelperTest {
    @Test
    fun `test fixedSeedHashOf`() {
        val actual = fixedSeedHashOf("Hello, World!")
        assertEquals(0x60415d5f616602aa, actual)
    }
}