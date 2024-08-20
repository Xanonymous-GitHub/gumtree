package antlr

import org.antlr.v4.runtime.NoViableAltException
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenStream
import org.antlr.v4.runtime.Vocabulary
import org.antlr.v4.runtime.VocabularyImpl
import org.antlr.v4.runtime.atn.ATN
import org.antlr.v4.runtime.atn.ATNDeserializer
import org.antlr.v4.runtime.atn.ParserATNSimulator
import org.antlr.v4.runtime.atn.PredictionContextCache
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.TerminalNode

@Suppress("unused")
class UrlParser(input: TokenStream) : Parser(input) {
    init {
        _interp = ParserATNSimulator(this, atn, decisionToDFA, sharedContextCache)
    }

    @Deprecated("", ReplaceWith("Companion.tokenNames", "antlr.UrlParser.Companion"))
    override fun getTokenNames(): Array<String?> = Companion.tokenNames

    override fun getVocabulary(): Vocabulary = VOCABULARY

    override fun getGrammarFileName(): String = "url.g4"

    override fun getRuleNames(): Array<String> = Companion.ruleNames

    override fun getSerializedATN(): String = SERIALIZED_ATN

    override fun getATN(): ATN = atn

    @Throws(RecognitionException::class)
    fun url(): UrlContext {
        val localctx = UrlContext(_ctx, state)
        enterRule(localctx, 0, RULE_URL)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 32
                uri()
                state = 33
                match(EOF)
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun uri(): UriContext {
        val localctx = UriContext(_ctx, state)
        enterRule(localctx, 2, RULE_URI)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 35
                scheme()
                state = 36
                match(T__0)
                state = 38
                _errHandler.sync(this)
                if (interpreter.adaptivePredict(_input, 0, _ctx) == 1) {
                    state = 37
                    login()
                }
                state = 40
                host()
                state = 43
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__1) {
                    run {
                        state = 41
                        match(T__1)
                        state = 42
                        port()
                    }
                }

                state = 49
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__2) {
                    run {
                        state = 45
                        match(T__2)
                        state = 47
                        _errHandler.sync(this)
                        la = _input.LA(1)
                        if (la == DIGITS || la == STRING) {
                            run {
                                state = 46
                                path()
                            }
                        }
                    }
                }

                state = 52
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__8) {
                    run {
                        state = 51
                        query()
                    }
                }

                state = 55
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__7) {
                    run {
                        state = 54
                        frag()
                    }
                }

                state = 58
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == WS) {
                    run {
                        state = 57
                        match(WS)
                    }
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun scheme(): SchemeContext {
        val localctx = SchemeContext(_ctx, state)
        enterRule(localctx, 4, RULE_SCHEME)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 60
                string()
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun host(): HostContext {
        val localctx = HostContext(_ctx, state)
        enterRule(localctx, 6, RULE_HOST)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 63
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__2) {
                    run {
                        state = 62
                        match(T__2)
                    }
                }

                state = 65
                hostname()
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun hostname(): HostnameContext {
        var localctx = HostnameContext(_ctx, state)
        enterRule(localctx, 8, RULE_HOSTNAME)
        try {
            state = 72
            _errHandler.sync(this)
            when (_input.LA(1)) {
                DIGITS, STRING -> {
                    localctx = DomainNameOrIPv4HostContext(localctx)
                    enterOuterAlt(localctx, 1)
                    run {
                        state = 67
                        string()
                    }
                }

                T__3 -> {
                    localctx = IPv6HostContext(localctx)
                    enterOuterAlt(localctx, 2)
                    run {
                        state = 68
                        match(T__3)
                        state = 69
                        v6host()
                        state = 70
                        match(T__4)
                    }
                }

                else -> throw NoViableAltException(this)
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun v6host(): V6hostContext {
        val localctx = V6hostContext(_ctx, state)
        enterRule(localctx, 10, RULE_V6HOST)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 75
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__5) {
                    run {
                        state = 74
                        match(T__5)
                    }
                }

                state = 79
                _errHandler.sync(this)
                when (interpreter.adaptivePredict(_input, 10, _ctx)) {
                    1 -> {
                        state = 77
                        string()
                    }

                    2 -> {
                        state = 78
                        match(DIGITS)
                    }
                }
                state = 88
                _errHandler.sync(this)
                la = _input.LA(1)
                while (la == T__1 || la == T__5) {
                    run {
                        run {
                            state = 81
                            la = _input.LA(1)
                            if (!(la == T__1 || la == T__5)) {
                                _errHandler.recoverInline(this)
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true
                                _errHandler.reportMatch(this)
                                consume()
                            }
                            state = 84
                            _errHandler.sync(this)
                            when (interpreter.adaptivePredict(_input, 11, _ctx)) {
                                1 -> {
                                    state = 82
                                    string()
                                }

                                2 -> {
                                    state = 83
                                    match(DIGITS)
                                }

                                else -> {}
                            }
                        }
                    }
                    state = 90
                    _errHandler.sync(this)
                    la = _input.LA(1)
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun port(): PortContext {
        val localctx = PortContext(_ctx, state)
        enterRule(localctx, 12, RULE_PORT)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 91
                match(DIGITS)
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun path(): PathContext {
        val localctx = PathContext(_ctx, state)
        enterRule(localctx, 14, RULE_PATH)
        var la: Int
        try {
            var alt: Int
            enterOuterAlt(localctx, 1)
            run {
                state = 93
                string()
                state = 98
                _errHandler.sync(this)
                alt = interpreter.adaptivePredict(_input, 13, _ctx)
                while (alt != 2 && alt != ATN.INVALID_ALT_NUMBER) {
                    if (alt == 1) {
                        run {
                            run {
                                state = 94
                                match(T__2)
                                state = 95
                                string()
                            }
                        }
                    }
                    state = 100
                    _errHandler.sync(this)
                    alt = interpreter.adaptivePredict(_input, 13, _ctx)
                }
                state = 102
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__2) {
                    run {
                        state = 101
                        match(T__2)
                    }
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun user(): UserContext {
        val localctx = UserContext(_ctx, state)
        enterRule(localctx, 16, RULE_USER)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 104
                string()
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun login(): LoginContext {
        val localctx = LoginContext(_ctx, state)
        enterRule(localctx, 18, RULE_LOGIN)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 106
                user()
                state = 109
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__1) {
                    run {
                        state = 107
                        match(T__1)
                        state = 108
                        password()
                    }
                }

                state = 111
                match(T__6)
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun password(): PasswordContext {
        val localctx = PasswordContext(_ctx, state)
        enterRule(localctx, 20, RULE_PASSWORD)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 113
                string()
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun frag(): FragContext {
        val localctx = FragContext(_ctx, state)
        enterRule(localctx, 22, RULE_FRAG)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 115
                match(T__7)
                state = 118
                _errHandler.sync(this)
                when (interpreter.adaptivePredict(_input, 16, _ctx)) {
                    1 -> {
                        state = 116
                        string()
                    }

                    2 -> {
                        state = 117
                        match(DIGITS)
                    }

                    else -> {}
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun query(): QueryContext {
        val localctx = QueryContext(_ctx, state)
        enterRule(localctx, 24, RULE_QUERY)
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 120
                match(T__8)
                state = 121
                search()
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun search(): SearchContext {
        val localctx = SearchContext(_ctx, state)
        enterRule(localctx, 26, RULE_SEARCH)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 123
                searchparameter()
                state = 128
                _errHandler.sync(this)
                la = _input.LA(1)
                while (la == T__9) {
                    run {
                        run {
                            state = 124
                            match(T__9)
                            state = 125
                            searchparameter()
                        }
                    }
                    state = 130
                    _errHandler.sync(this)
                    la = _input.LA(1)
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun searchparameter(): SearchparameterContext {
        val localctx = SearchparameterContext(_ctx, state)
        enterRule(localctx, 28, RULE_SEARCH_PARAMETER)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 131
                string()
                state = 138
                _errHandler.sync(this)
                la = _input.LA(1)
                if (la == T__10) {
                    run {
                        state = 132
                        match(T__10)
                        state = 136
                        _errHandler.sync(this)
                        when (interpreter.adaptivePredict(_input, 18, _ctx)) {
                            1 -> {
                                state = 133
                                string()
                            }

                            2 -> {
                                state = 134
                                match(DIGITS)
                            }

                            3 -> {
                                state = 135
                                match(HEX)
                            }

                            else -> {}
                        }
                    }
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    @Throws(RecognitionException::class)
    fun string(): StringContext {
        val localctx = StringContext(_ctx, state)
        enterRule(localctx, 30, RULE_STRING)
        var la: Int
        try {
            enterOuterAlt(localctx, 1)
            run {
                state = 140
                la = _input.LA(1)
                if (!(la == DIGITS || la == STRING)) {
                    _errHandler.recoverInline(this)
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true
                    _errHandler.reportMatch(this)
                    consume()
                }
            }
        } catch (re: RecognitionException) {
            localctx.exception = re
            _errHandler.reportError(this, re)
            _errHandler.recover(this, re)
        } finally {
            exitRule()
        }
        return localctx
    }

    class UrlContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun uri(): UriContext {
            return getRuleContext(UriContext::class.java, 0)
        }

        fun eof(): TerminalNode {
            return getToken(EOF, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_URL
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterUrl(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitUrl(this)
        }
    }

    class UriContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun scheme(): SchemeContext {
            return getRuleContext(SchemeContext::class.java, 0)
        }

        fun host(): HostContext {
            return getRuleContext(HostContext::class.java, 0)
        }

        fun login(): LoginContext {
            return getRuleContext(LoginContext::class.java, 0)
        }

        fun port(): PortContext {
            return getRuleContext(PortContext::class.java, 0)
        }

        fun query(): QueryContext {
            return getRuleContext(QueryContext::class.java, 0)
        }

        fun frag(): FragContext {
            return getRuleContext(FragContext::class.java, 0)
        }

        fun ws(): TerminalNode {
            return getToken(WS, 0)
        }

        fun path(): PathContext {
            return getRuleContext(PathContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_URI
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterUri(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitUri(this)
        }
    }

    class SchemeContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): StringContext {
            return getRuleContext(StringContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_SCHEME
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterScheme(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitScheme(this)
        }
    }

    class HostContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun hostname(): HostnameContext {
            return getRuleContext(HostnameContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_HOST
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterHost(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitHost(this)
        }
    }

    open class HostnameContext : ParserRuleContext {
        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState)

        constructor()

        override fun getRuleIndex(): Int {
            return RULE_HOSTNAME
        }

        fun copyFrom(ctx: HostnameContext?) {
            super.copyFrom(ctx)
        }
    }

    class IPv6HostContext(ctx: HostnameContext?) : HostnameContext() {
        init {
            copyFrom(ctx)
        }

        fun v6host(): V6hostContext {
            return getRuleContext(V6hostContext::class.java, 0)
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterIPv6Host(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitIPv6Host(this)
        }
    }

    class DomainNameOrIPv4HostContext(ctx: HostnameContext?) : HostnameContext() {
        init {
            copyFrom(ctx)
        }

        fun string(): StringContext {
            return getRuleContext(StringContext::class.java, 0)
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterDomainNameOrIPv4Host(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitDomainNameOrIPv4Host(this)
        }
    }

    class V6hostContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): List<StringContext> {
            return getRuleContexts(StringContext::class.java)
        }

        fun string(i: Int): StringContext {
            return getRuleContext(StringContext::class.java, i)
        }

        fun digits(): List<TerminalNode> {
            return getTokens(DIGITS)
        }

        fun digits(i: Int): TerminalNode {
            return getToken(DIGITS, i)
        }

        override fun getRuleIndex(): Int {
            return RULE_V6HOST
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterV6host(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitV6host(this)
        }
    }

    class PortContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun digits(): TerminalNode {
            return getToken(DIGITS, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_PORT
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterPort(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitPort(this)
        }
    }

    class PathContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): List<StringContext> {
            return getRuleContexts(StringContext::class.java)
        }

        fun string(i: Int): StringContext {
            return getRuleContext(StringContext::class.java, i)
        }

        override fun getRuleIndex(): Int {
            return RULE_PATH
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterPath(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitPath(this)
        }
    }

    class UserContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): StringContext {
            return getRuleContext(StringContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_USER
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterUser(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitUser(this)
        }
    }

    class LoginContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun user(): UserContext {
            return getRuleContext(UserContext::class.java, 0)
        }

        fun password(): PasswordContext {
            return getRuleContext(PasswordContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_LOGIN
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterLogin(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitLogin(this)
        }
    }

    class PasswordContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): StringContext {
            return getRuleContext(StringContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_PASSWORD
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterPassword(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitPassword(this)
        }
    }

    class FragContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): StringContext {
            return getRuleContext(StringContext::class.java, 0)
        }

        fun digits(): TerminalNode {
            return getToken(DIGITS, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_FRAG
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterFrag(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitFrag(this)
        }
    }

    class QueryContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun search(): SearchContext {
            return getRuleContext(SearchContext::class.java, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_QUERY
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterQuery(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitQuery(this)
        }
    }

    class SearchContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun searchparameter(): List<SearchparameterContext> {
            return getRuleContexts(SearchparameterContext::class.java)
        }

        fun searchparameter(i: Int): SearchparameterContext {
            return getRuleContext(SearchparameterContext::class.java, i)
        }

        override fun getRuleIndex(): Int {
            return RULE_SEARCH
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterSearch(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitSearch(this)
        }
    }

    class SearchparameterContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun string(): List<StringContext> {
            return getRuleContexts(StringContext::class.java)
        }

        fun string(i: Int): StringContext {
            return getRuleContext(StringContext::class.java, i)
        }

        fun digits(): TerminalNode {
            return getToken(DIGITS, 0)
        }

        fun hex(): TerminalNode {
            return getToken(HEX, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_SEARCH_PARAMETER
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterSearchparameter(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitSearchparameter(this)
        }
    }

    class StringContext(parent: ParserRuleContext?, invokingState: Int) :
        ParserRuleContext(parent, invokingState) {
        fun theString(): TerminalNode {
            return getToken(STRING, 0)
        }

        fun digits(): TerminalNode {
            return getToken(DIGITS, 0)
        }

        override fun getRuleIndex(): Int {
            return RULE_STRING
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.enterString(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is UrlListener) listener.exitString(this)
        }
    }

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
        const val RULE_URL: Int = 0
        const val RULE_URI: Int = 1
        const val RULE_SCHEME: Int = 2
        const val RULE_HOST: Int = 3
        const val RULE_HOSTNAME: Int = 4
        const val RULE_V6HOST: Int = 5
        const val RULE_PORT: Int = 6
        const val RULE_PATH: Int = 7
        const val RULE_USER: Int = 8
        const val RULE_LOGIN: Int = 9
        const val RULE_PASSWORD: Int = 10
        const val RULE_FRAG: Int = 11
        const val RULE_QUERY: Int = 12
        const val RULE_SEARCH: Int = 13
        const val RULE_SEARCH_PARAMETER: Int = 14
        const val RULE_STRING: Int = 15
        val ruleNames: Array<String> = makeRuleNames()
        val tokenNames: Array<String?>
        private const val SERIALIZED_ATN: String =
            "\u0004\u0001\u000f\u008f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001" +
                    "\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004" +
                    "\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007" +
                    "\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b" +
                    "\u0002\u000c\u0007\u000c\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007" +
                    "\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0003\u0001'\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003" +
                    "\u0001,\b\u0001\u0001\u0001\u0001\u0001\u0003\u00010\b\u0001\u0003\u0001" +
                    "2\b\u0001\u0001\u0001\u0003\u00015\b\u0001\u0001\u0001\u0003\u00018\b" +
                    "\u0001\u0001\u0001\u0003\u0001;\b\u0001\u0001\u0002\u0001\u0002\u0001" +
                    "\u0003\u0003\u0003@\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001" +
                    "\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004I\b\u0004\u0001" +
                    "\u0005\u0003\u0005L\b\u0005\u0001\u0005\u0001\u0005\u0003\u0005P\b\u0005" +
                    "\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005U\b\u0005\u0005\u0005" +
                    "W\b\u0005\n\u0005\u000c\u0005Z\t\u0005\u0001\u0006\u0001\u0006\u0001\u0007" +
                    "\u0001\u0007\u0001\u0007\u0005\u0007a\b\u0007\n\u0007\u000c\u0007d\t\u0007" +
                    "\u0001\u0007\u0003\u0007g\b\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001" +
                    "\t\u0003\tn\b\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b" +
                    "\u0001\u000b\u0003\u000bw\b\u000b\u0001\u000c\u0001\u000c\u0001\u000c\u0001\r\u0001" +
                    "\r\u0001\r\u0005\r\u007f\b\r\n\r\u000c\r\u0082\t\r\u0001\u000e\u0001\u000e" +
                    "\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u0089\b\u000e\u0003\u000e" +
                    "\u008b\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0000\u0000\u0010\u0000" +
                    "\u0002\u0004\u0006\b\n\u000c\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c" +
                    "\u001e\u0000\u0002\u0002\u0000\u0002\u0002\u0006\u0006\u0002\u0000\u000c\u000c" +
                    "\u000e\u000e\u0093\u0000 \u0001\u0000\u0000\u0000\u0002#\u0001\u0000\u0000" +
                    "\u0000\u0004<\u0001\u0000\u0000\u0000\u0006?\u0001\u0000\u0000\u0000\b" +
                    "H\u0001\u0000\u0000\u0000\nK\u0001\u0000\u0000\u0000\u000c[\u0001\u0000\u0000" +
                    "\u0000\u000e]\u0001\u0000\u0000\u0000\u0010h\u0001\u0000\u0000\u0000\u0012" +
                    "j\u0001\u0000\u0000\u0000\u0014q\u0001\u0000\u0000\u0000\u0016s\u0001" +
                    "\u0000\u0000\u0000\u0018x\u0001\u0000\u0000\u0000\u001a{\u0001\u0000\u0000" +
                    "\u0000\u001c\u0083\u0001\u0000\u0000\u0000\u001e\u008c\u0001\u0000\u0000" +
                    "\u0000 !\u0003\u0002\u0001\u0000!\"\u0005\u0000\u0000\u0001\"\u0001\u0001" +
                    "\u0000\u0000\u0000#$\u0003\u0004\u0002\u0000$&\u0005\u0001\u0000\u0000" +
                    "%'\u0003\u0012\t\u0000&%\u0001\u0000\u0000\u0000&'\u0001\u0000\u0000" +
                    "\u0000'(\u0001\u0000\u0000\u0000(+\u0003\u0006\u0003\u0000)*\u0005\u0002" +
                    "\u0000\u0000*,\u0003\u000c\u0006\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000" +
                    "\u0000\u0000,1\u0001\u0000\u0000\u0000-/\u0005\u0003\u0000\u0000.0\u0003" +
                    "\u000e\u0007\u0000/.\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u0000" +
                    "02\u0001\u0000\u0000\u00001-\u0001\u0000\u0000\u000012\u0001\u0000\u0000" +
                    "\u000024\u0001\u0000\u0000\u000035\u0003\u0018\u000c\u000043\u0001\u0000\u0000" +
                    "\u000045\u0001\u0000\u0000\u000057\u0001\u0000\u0000\u000068\u0003\u0016" +
                    "\u000b\u000076\u0001\u0000\u0000\u000078\u0001\u0000\u0000\u00008:\u0001" +
                    "\u0000\u0000\u00009;\u0005\u000f\u0000\u0000:9\u0001\u0000\u0000\u0000" +
                    ":;\u0001\u0000\u0000\u0000;\u0003\u0001\u0000\u0000\u0000<=\u0003\u001e" +
                    "\u000f\u0000=\u0005\u0001\u0000\u0000\u0000>@\u0005\u0003\u0000\u0000" +
                    "?>\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000" +
                    "\u0000AB\u0003\b\u0004\u0000B\u0007\u0001\u0000\u0000\u0000CI\u0003\u001e" +
                    "\u000f\u0000DE\u0005\u0004\u0000\u0000EF\u0003\n\u0005\u0000FG\u0005\u0005" +
                    "\u0000\u0000GI\u0001\u0000\u0000\u0000HC\u0001\u0000\u0000\u0000HD\u0001" +
                    "\u0000\u0000\u0000I\t\u0001\u0000\u0000\u0000JL\u0005\u0006\u0000\u0000" +
                    "KJ\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LO\u0001\u0000\u0000" +
                    "\u0000MP\u0003\u001e\u000f\u0000NP\u0005\u000c\u0000\u0000OM\u0001\u0000\u0000" +
                    "\u0000ON\u0001\u0000\u0000\u0000PX\u0001\u0000\u0000\u0000QT\u0007\u0000" +
                    "\u0000\u0000RU\u0003\u001e\u000f\u0000SU\u0005\u000c\u0000\u0000TR\u0001\u0000" +
                    "\u0000\u0000TS\u0001\u0000\u0000\u0000UW\u0001\u0000\u0000\u0000VQ\u0001" +
                    "\u0000\u0000\u0000WZ\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000" +
                    "XY\u0001\u0000\u0000\u0000Y\u000b\u0001\u0000\u0000\u0000ZX\u0001\u0000" +
                    "\u0000\u0000[\\\u0005\u000c\u0000\u0000\\\r\u0001\u0000\u0000\u0000]b\u0003" +
                    "\u001e\u000f\u0000^_\u0005\u0003\u0000\u0000_a\u0003\u001e\u000f\u0000" +
                    "`^\u0001\u0000\u0000\u0000ad\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000" +
                    "\u0000bc\u0001\u0000\u0000\u0000cf\u0001\u0000\u0000\u0000db\u0001\u0000" +
                    "\u0000\u0000eg\u0005\u0003\u0000\u0000fe\u0001\u0000\u0000\u0000fg\u0001" +
                    "\u0000\u0000\u0000g\u000f\u0001\u0000\u0000\u0000hi\u0003\u001e\u000f" +
                    "\u0000i\u0011\u0001\u0000\u0000\u0000jm\u0003\u0010\b\u0000kl\u0005\u0002" +
                    "\u0000\u0000ln\u0003\u0014\n\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000" +
                    "\u0000\u0000no\u0001\u0000\u0000\u0000op\u0005\u0007\u0000\u0000p\u0013" +
                    "\u0001\u0000\u0000\u0000qr\u0003\u001e\u000f\u0000r\u0015\u0001\u0000" +
                    "\u0000\u0000sv\u0005\b\u0000\u0000tw\u0003\u001e\u000f\u0000uw\u0005\u000c" +
                    "\u0000\u0000vt\u0001\u0000\u0000\u0000vu\u0001\u0000\u0000\u0000w\u0017" +
                    "\u0001\u0000\u0000\u0000xy\u0005\t\u0000\u0000yz\u0003\u001a\r\u0000z" +
                    "\u0019\u0001\u0000\u0000\u0000{\u0080\u0003\u001c\u000e\u0000|}\u0005" +
                    "\n\u0000\u0000}\u007f\u0003\u001c\u000e\u0000~|\u0001\u0000\u0000\u0000" +
                    "\u007f\u0082\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0080" +
                    "\u0081\u0001\u0000\u0000\u0000\u0081\u001b\u0001\u0000\u0000\u0000\u0082" +
                    "\u0080\u0001\u0000\u0000\u0000\u0083\u008a\u0003\u001e\u000f\u0000\u0084" +
                    "\u0088\u0005\u000b\u0000\u0000\u0085\u0089\u0003\u001e\u000f\u0000\u0086" +
                    "\u0089\u0005\u000c\u0000\u0000\u0087\u0089\u0005\r\u0000\u0000\u0088\u0085" +
                    "\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0087" +
                    "\u0001\u0000\u0000\u0000\u0089\u008b\u0001\u0000\u0000\u0000\u008a\u0084" +
                    "\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u001d" +
                    "\u0001\u0000\u0000\u0000\u008c\u008d\u0007\u0001\u0000\u0000\u008d\u001f" +
                    "\u0001\u0000\u0000\u0000\u0014&+/147:?HKOTXbfmv\u0080\u0088\u008a"
        private val atn: ATN = ATNDeserializer().deserialize(SERIALIZED_ATN.toCharArray())
        val decisionToDFA: Array<DFA?> = arrayOfNulls(atn.numberOfDecisions)
        val sharedContextCache: PredictionContextCache = PredictionContextCache()
        private val _LITERAL_NAMES = makeLiteralNames()
        private val _SYMBOLIC_NAMES = makeSymbolicNames()
        val VOCABULARY: Vocabulary = VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES)

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
                "url", "uri", "scheme", "host", "hostname", "v6host", "port", "path",
                "user", "login", "password", "frag", "query", "search", "searchparameter",
                "string"
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