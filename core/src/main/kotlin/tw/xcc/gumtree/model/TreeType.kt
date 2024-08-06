package tw.xcc.gumtree.model

import java.io.Serializable

/**
 * TreeType corresponds to the name of their production rule in the grammar of an AST.
 * e.g. `IfStatement`, `FunctionStatement`
 * */
data class TreeType(val name: String) : Serializable {
    fun isEmpty(): Boolean = name.isEmpty()

    companion object {
        fun empty(): TreeType = TreeType("")
    }
}