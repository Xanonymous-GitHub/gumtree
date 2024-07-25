import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.algorithms.lcsBaseOnlySize
import tw.xcc.gumtree.matchers.algorithms.lcsBaseWithElements
import kotlin.test.Test
import kotlin.test.assertEquals

@Execution(ExecutionMode.CONCURRENT)
internal class LCSTest {
    @Test
    fun `test lcs only size`() {
        val a = "AGGTAB"
        val b = "GXTXAYB"
        val expectLCS = 4 // "GTAB"
        val actualLCS = lcsBaseOnlySize(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs only size with empty string`() {
        val a = ""
        val b = "GXTXAYB"
        val expectLCS = 0
        val actualLCS = lcsBaseOnlySize(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs only size with same string`() {
        val a = "AGGTAB"
        val b = "AGGTAB"
        val expectLCS = 6
        val actualLCS = lcsBaseOnlySize(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs with return elements`() {
        val a = "AGGTAB"
        val b = "GXTXAYB"
        val expectLCS = "GTAB".toList()
        val actualLCS = lcsBaseWithElements(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs with return elements with empty string`() {
        val a = ""
        val b = "GXTXAYB"
        val expectLCS = emptyList<Char>()
        val actualLCS = lcsBaseWithElements(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs with return elements with same string`() {
        val a = "AGGTAB"
        val b = "AGGTAB"
        val expectLCS = "AGGTAB".toList()
        val actualLCS = lcsBaseWithElements(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs with return elements with same string and different order`() {
        val a = "AGGTAB"
        val b = "BATGGA"
        val expectLCS = "AGGA".toList()
        val actualLCS = lcsBaseWithElements(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }

    @Test
    fun `test lcs with return elements but no common elements`() {
        val a = "AGGTAB"
        val b = "xxxxxxxxx"
        val expectLCS = emptyList<Char>()
        val actualLCS = lcsBaseWithElements(a.toList(), b.toList())
        assertEquals(expectLCS, actualLCS)
    }
}