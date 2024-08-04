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
import java.io.InputStream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class GumTreeConverter(private val vocabulary: Vocabulary) {
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
        inputStream: InputStream,
        parseTreeCreation: (CharStream) -> ParserRuleContext
    ): GumTree? {
        contract {
            callsInPlace(parseTreeCreation, InvocationKind.AT_MOST_ONCE)
        }

        return coroutineScope {
            val charStream =
                withContext(Dispatchers.IO) {
                    CharStreams.fromStream(inputStream)
                }

            val firstGrammarEntry = parseTreeCreation(charStream)
            buildWholeGumTreeFrom(firstGrammarEntry)
        }
    }
}