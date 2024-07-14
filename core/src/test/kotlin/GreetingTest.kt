import kotlin.test.Test
import kotlin.test.assertEquals

internal class GreetingTest {
    private val greeting = Greeting()

    @Test
    fun testGreet() {
        assertEquals("Hello, Java ${System.getProperty("java.version")}!", greeting.greet())
    }
}