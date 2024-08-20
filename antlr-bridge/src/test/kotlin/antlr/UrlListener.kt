// Generated from url.g4 by ANTLR 4.13.2
package antlr

import org.antlr.v4.runtime.tree.ParseTreeListener

/**
 * This interface defines a complete listener for a parse tree produced by
 * [UrlParser].
 */
internal interface UrlListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [UrlParser.url].
     * @param ctx the parse tree
     */
    fun enterUrl(ctx: UrlParser.UrlContext?)

    /**
     * Exit a parse tree produced by [UrlParser.url].
     * @param ctx the parse tree
     */
    fun exitUrl(ctx: UrlParser.UrlContext?)

    /**
     * Enter a parse tree produced by [UrlParser.uri].
     * @param ctx the parse tree
     */
    fun enterUri(ctx: UrlParser.UriContext?)

    /**
     * Exit a parse tree produced by [UrlParser.uri].
     * @param ctx the parse tree
     */
    fun exitUri(ctx: UrlParser.UriContext?)

    /**
     * Enter a parse tree produced by [UrlParser.scheme].
     * @param ctx the parse tree
     */
    fun enterScheme(ctx: UrlParser.SchemeContext?)

    /**
     * Exit a parse tree produced by [UrlParser.scheme].
     * @param ctx the parse tree
     */
    fun exitScheme(ctx: UrlParser.SchemeContext?)

    /**
     * Enter a parse tree produced by [UrlParser.host].
     * @param ctx the parse tree
     */
    fun enterHost(ctx: UrlParser.HostContext?)

    /**
     * Exit a parse tree produced by [UrlParser.host].
     * @param ctx the parse tree
     */
    fun exitHost(ctx: UrlParser.HostContext?)

    /**
     * Enter a parse tree produced by the `DomainNameOrIPv4Host`
     * labeled alternative in [UrlParser.hostname].
     * @param ctx the parse tree
     */
    fun enterDomainNameOrIPv4Host(ctx: UrlParser.DomainNameOrIPv4HostContext?)

    /**
     * Exit a parse tree produced by the `DomainNameOrIPv4Host`
     * labeled alternative in [UrlParser.hostname].
     * @param ctx the parse tree
     */
    fun exitDomainNameOrIPv4Host(ctx: UrlParser.DomainNameOrIPv4HostContext?)

    /**
     * Enter a parse tree produced by the `IPv6Host`
     * labeled alternative in [UrlParser.hostname].
     * @param ctx the parse tree
     */
    fun enterIPv6Host(ctx: UrlParser.IPv6HostContext?)

    /**
     * Exit a parse tree produced by the `IPv6Host`
     * labeled alternative in [UrlParser.hostname].
     * @param ctx the parse tree
     */
    fun exitIPv6Host(ctx: UrlParser.IPv6HostContext?)

    /**
     * Enter a parse tree produced by [UrlParser.v6host].
     * @param ctx the parse tree
     */
    fun enterV6host(ctx: UrlParser.V6hostContext?)

    /**
     * Exit a parse tree produced by [UrlParser.v6host].
     * @param ctx the parse tree
     */
    fun exitV6host(ctx: UrlParser.V6hostContext?)

    /**
     * Enter a parse tree produced by [UrlParser.port].
     * @param ctx the parse tree
     */
    fun enterPort(ctx: UrlParser.PortContext?)

    /**
     * Exit a parse tree produced by [UrlParser.port].
     * @param ctx the parse tree
     */
    fun exitPort(ctx: UrlParser.PortContext?)

    /**
     * Enter a parse tree produced by [UrlParser.path].
     * @param ctx the parse tree
     */
    fun enterPath(ctx: UrlParser.PathContext?)

    /**
     * Exit a parse tree produced by [UrlParser.path].
     * @param ctx the parse tree
     */
    fun exitPath(ctx: UrlParser.PathContext?)

    /**
     * Enter a parse tree produced by [UrlParser.user].
     * @param ctx the parse tree
     */
    fun enterUser(ctx: UrlParser.UserContext?)

    /**
     * Exit a parse tree produced by [UrlParser.user].
     * @param ctx the parse tree
     */
    fun exitUser(ctx: UrlParser.UserContext?)

    /**
     * Enter a parse tree produced by [UrlParser.login].
     * @param ctx the parse tree
     */
    fun enterLogin(ctx: UrlParser.LoginContext?)

    /**
     * Exit a parse tree produced by [UrlParser.login].
     * @param ctx the parse tree
     */
    fun exitLogin(ctx: UrlParser.LoginContext?)

    /**
     * Enter a parse tree produced by [UrlParser.password].
     * @param ctx the parse tree
     */
    fun enterPassword(ctx: UrlParser.PasswordContext?)

    /**
     * Exit a parse tree produced by [UrlParser.password].
     * @param ctx the parse tree
     */
    fun exitPassword(ctx: UrlParser.PasswordContext?)

    /**
     * Enter a parse tree produced by [UrlParser.frag].
     * @param ctx the parse tree
     */
    fun enterFrag(ctx: UrlParser.FragContext?)

    /**
     * Exit a parse tree produced by [UrlParser.frag].
     * @param ctx the parse tree
     */
    fun exitFrag(ctx: UrlParser.FragContext?)

    /**
     * Enter a parse tree produced by [UrlParser.query].
     * @param ctx the parse tree
     */
    fun enterQuery(ctx: UrlParser.QueryContext?)

    /**
     * Exit a parse tree produced by [UrlParser.query].
     * @param ctx the parse tree
     */
    fun exitQuery(ctx: UrlParser.QueryContext?)

    /**
     * Enter a parse tree produced by [UrlParser.search].
     * @param ctx the parse tree
     */
    fun enterSearch(ctx: UrlParser.SearchContext?)

    /**
     * Exit a parse tree produced by [UrlParser.search].
     * @param ctx the parse tree
     */
    fun exitSearch(ctx: UrlParser.SearchContext?)

    /**
     * Enter a parse tree produced by [UrlParser.searchparameter].
     * @param ctx the parse tree
     */
    fun enterSearchparameter(ctx: UrlParser.SearchparameterContext?)

    /**
     * Exit a parse tree produced by [UrlParser.searchparameter].
     * @param ctx the parse tree
     */
    fun exitSearchparameter(ctx: UrlParser.SearchparameterContext?)

    /**
     * Enter a parse tree produced by [UrlParser.string].
     * @param ctx the parse tree
     */
    fun enterString(ctx: UrlParser.StringContext?)

    /**
     * Exit a parse tree produced by [UrlParser.string].
     * @param ctx the parse tree
     */
    fun exitString(ctx: UrlParser.StringContext?)
}