import antlr.UrlLexer
import antlr.UrlParser
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.antlrBridge.GumTreeConverter
import java.io.File
import kotlin.test.Test

@Execution(ExecutionMode.CONCURRENT)
class GumTreeConverterTest {
    @Test
    fun `test buildWholeGumTreeFrom`() {
        runBlocking {
            val file = File("src/test/kotlin/data/urls.txt")
            val inputStream = file.inputStream()

            assertDoesNotThrow {
                // TODO: implement the test logic
                GumTreeConverter.convertFrom(inputStream, ::UrlLexer, ::UrlParser, UrlParser::url)
            }
        }
    }
}