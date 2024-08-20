package antlr

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.VocabularyImpl
import org.antlr.v4.runtime.atn.ATN
import org.antlr.v4.runtime.atn.ATNDeserializer
import org.antlr.v4.runtime.atn.LexerATNSimulator
import org.antlr.v4.runtime.atn.PredictionContextCache
import org.antlr.v4.runtime.dfa.DFA

@Suppress("unused")
class UrlLexer(input: CharStream?) : Lexer(input) {
    init {
        _interp = LexerATNSimulator(this, atn, decisionToDFA, sharedContextCache)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("Companion.tokenNames", "antlr.UrlLexer.Companion")
    )
    override fun getTokenNames(): Array<String?> = UrlLexer.tokenNames

    override fun getVocabulary(): Vocabulary = VOCABULARY

    override fun getGrammarFileName(): String = "url.g4"

    override fun getRuleNames(): Array<String> = UrlLexer.ruleNames

    override fun getSerializedATN(): String = SERIALIZED_ATN

    override fun getChannelNames(): Array<String> = UrlLexer.channelNames

    override fun getModeNames(): Array<String> = UrlLexer.modeNames

    override fun getATN(): ATN = atn

    companion object {
        const val T__0: Int = 1
        const val T__1: Int = 2
        const val T__2: Int = 3
        const val T__3: Int = 4
        const val T__4: Int = 5
        const val T__5: Int = 6
        const val T__6: Int = 7
        const val T__7: Int = 8
        const val T__8: Int = 9
        const val T__9: Int = 10
        const val T__10: Int = 11
        const val DIGITS: Int = 12
        const val HEX: Int = 13
        const val STRING: Int = 14
        const val WS: Int = 15
        val ruleNames: Array<String> = makeRuleNames()
        val tokenNames: Array<String?>
        const val SERIALIZED_ATN: String =
            "\u0004\u0000\u000fT\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001" +
                "\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004" +
                "\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007" +
                "\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b" +
                "\u0007\u000b\u0002\u000c\u0007\u000c\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001" +
                "\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001" +
                "\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001" +
                "\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001" +
                "\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0004" +
                "\u000b:\b\u000b\u000b\u000b\u000c\u000b;\u0001\u000c\u0001\u000c\u0001\u000c\u0004\u000cA" +
                "\b\u000c\u000b\u000c\u000c\u000cB\u0001\r\u0001\r\u0003\rG\b\r\u0001\r\u0001\r\u0005\r" +
                "K\b\r\n\r\u000c\rN\t\r\u0001\u000e\u0004\u000eQ\b\u000e\u000b\u000e\u000c\u000e" +
                "R\u0000\u0000\u000f\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t" +
                "\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\u000c" +
                "\u0019\r\u001b\u000e\u001d\u000f\u0001\u0000\u0005\u0001\u000009\u0003" +
                "\u000009AFaf\u0004\u000009AZaz~~\u0005\u0000++-.09AZaz\u0002\u0000\n\n" +
                "\r\rY\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000" +
                "\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000" +
                "\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000" +
                "\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000" +
                "\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000" +
                "\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000" +
                "\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000" +
                "\u001d\u0001\u0000\u0000\u0000\u0001\u001f\u0001\u0000\u0000\u0000\u0003" +
                "#\u0001\u0000\u0000\u0000\u0005%\u0001\u0000\u0000\u0000\u0007\'\u0001" +
                "\u0000\u0000\u0000\t)\u0001\u0000\u0000\u0000\u000b+\u0001\u0000\u0000" +
                "\u0000\r.\u0001\u0000\u0000\u0000\u000f0\u0001\u0000\u0000\u0000\u0011" +
                "2\u0001\u0000\u0000\u0000\u00134\u0001\u0000\u0000\u0000\u00156\u0001" +
                "\u0000\u0000\u0000\u00179\u0001\u0000\u0000\u0000\u0019@\u0001\u0000\u0000" +
                "\u0000\u001bF\u0001\u0000\u0000\u0000\u001dP\u0001\u0000\u0000\u0000\u001f" +
                " \u0005:\u0000\u0000 !\u0005/\u0000\u0000!\"\u0005/\u0000\u0000\"\u0002" +
                "\u0001\u0000\u0000\u0000#$\u0005:\u0000\u0000$\u0004\u0001\u0000\u0000" +
                "\u0000%&\u0005/\u0000\u0000&\u0006\u0001\u0000\u0000\u0000\'(\u0005[\u0000" +
                "\u0000(\b\u0001\u0000\u0000\u0000)*\u0005]\u0000\u0000*\n\u0001\u0000" +
                "\u0000\u0000+,\u0005:\u0000\u0000,-\u0005:\u0000\u0000-\u000c\u0001\u0000" +
                "\u0000\u0000./\u0005@\u0000\u0000/\u000e\u0001\u0000\u0000\u000001\u0005" +
                "#\u0000\u00001\u0010\u0001\u0000\u0000\u000023\u0005?\u0000\u00003\u0012" +
                "\u0001\u0000\u0000\u000045\u0005&\u0000\u00005\u0014\u0001\u0000\u0000" +
                "\u000067\u0005=\u0000\u00007\u0016\u0001\u0000\u0000\u00008:\u0007\u0000" +
                "\u0000\u000098\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;9\u0001" +
                "\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<\u0018\u0001\u0000\u0000" +
                "\u0000=>\u0005%\u0000\u0000>?\u0007\u0001\u0000\u0000?A\u0007\u0001\u0000" +
                "\u0000@=\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000B@\u0001\u0000" +
                "\u0000\u0000BC\u0001\u0000\u0000\u0000C\u001a\u0001\u0000\u0000\u0000" +
                "DG\u0007\u0002\u0000\u0000EG\u0003\u0019\u000c\u0000FD\u0001\u0000\u0000\u0000" +
                "FE\u0001\u0000\u0000\u0000GL\u0001\u0000\u0000\u0000HK\u0007\u0003\u0000" +
                "\u0000IK\u0003\u0019\u000c\u0000JH\u0001\u0000\u0000\u0000JI\u0001\u0000\u0000" +
                "\u0000KN\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000LM\u0001\u0000" +
                "\u0000\u0000M\u001c\u0001\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000" +
                "OQ\u0007\u0004\u0000\u0000PO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000" +
                "\u0000RP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000S\u001e\u0001" +
                "\u0000\u0000\u0000\u0007\u0000;BFJLR\u0000"

        private val atn: ATN = ATNDeserializer().deserialize(SERIALIZED_ATN.toCharArray())
        val decisionToDFA: Array<DFA?> = arrayOfNulls(atn.numberOfDecisions)
        val sharedContextCache: PredictionContextCache = PredictionContextCache()
        private val _LITERAL_NAMES = makeLiteralNames()
        private val _SYMBOLIC_NAMES = makeSymbolicNames()
        val VOCABULARY: Vocabulary = VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES)
        var channelNames: Array<String> = arrayOf("DEFAULT_TOKEN_CHANNEL", "HIDDEN")
        var modeNames: Array<String> = arrayOf("DEFAULT_MODE")

        init {
            tokenNames = arrayOfNulls(_SYMBOLIC_NAMES.size)
            for (i in tokenNames.indices) {
                tokenNames[i] = VOCABULARY.getLiteralName(i)
            }
        }

        init {
            for (i in 0 until atn.numberOfDecisions) {
                decisionToDFA[i] = DFA(atn.getDecisionState(i), i)
            }
        }

        private fun makeRuleNames(): Array<String> =
            arrayOf(
                "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
                "T__9", "T__10", "DIGITS", "HEX", "STRING", "WS"
            )

        private fun makeLiteralNames(): Array<String?> =
            arrayOf(
                null, "'://'", "':'", "'/'", "'['", "']'", "'::'", "'@'", "'#'", "'?'",
                "'&'", "'='"
            )

        private fun makeSymbolicNames(): Array<String?> =
            arrayOf(
                null, null, null, null, null, null, null, null, null, null, null, null,
                "DIGITS", "HEX", "STRING", "WS"
            )
    }
}