package tw.xcc.gumtree.antlrBridge

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.tree.Tree
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.TreeType
import java.io.File
import java.io.InputStream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class GumTreeConverter(private val vocabulary: Vocabulary) {
    private fun fileStreamOf(path: String): InputStream {
        val file = File(path)
        val isValidToRead =
            file.isValidToRead(
                maxSizeInBytes = DEFAULT_ALLOWED_MAX_FILE_SIZE_IN_BYTE,
                allowedDirectory = file.parent,
                allowedExtensions = setOf(file.extension)
            )

        if (!isValidToRead) {
            throw IllegalArgumentException("File is not valid to read")
        }

        return file.inputStream()
    }

    private fun createSingleGumTreeNodeFrom(token: Token): GumTree =
        GumTree(
            GumTree.Info(
                type = TreeType(vocabulary.getSymbolicName(token.type) ?: "<UNKNOWN[${token.type}]>"),
                text = token.toString(),
                line = token.line,
                posOfLine = token.charPositionInLine
            )
        )

    private suspend fun buildWholeGumTreeFrom(antlrTree: Tree): GumTree? =
        coroutineScope {
            val self =
                when {
                    antlrTree is Token -> createSingleGumTreeNodeFrom(antlrTree)
                    else -> null
                } ?: return@coroutineScope null

            with(antlrTree) {
                val buildChildJobs = mutableListOf<Deferred<GumTree?>>()
                for (childIdx in 0 until childCount) {
                    buildChildJobs.add(
                        async { buildWholeGumTreeFrom(getChild(childIdx)) }
                    )
                }
                self.setChildrenTo(
                    buildChildJobs.awaitAll().filterNotNull()
                )
            }

            return@coroutineScope self
        }

    @OptIn(ExperimentalContracts::class)
    suspend fun convertFrom(
        filePath: String,
        parseTreeCreation: (CharStream) -> ParserRuleContext
    ): GumTree? {
        contract {
            callsInPlace(parseTreeCreation, InvocationKind.AT_MOST_ONCE)
        }

        return coroutineScope {
            val charStream =
                withContext(Dispatchers.IO) {
                    val inputStream = fileStreamOf(filePath)
                    CharStreams.fromStream(inputStream)
                }

            val firstGrammarEntry = parseTreeCreation(charStream)
            buildWholeGumTreeFrom(firstGrammarEntry)
        }
    }

    companion object {
        private const val DEFAULT_ALLOWED_MAX_FILE_SIZE_IN_BYTE = 1L * 1024 * 1024 * 1024 // 1GB
    }
}