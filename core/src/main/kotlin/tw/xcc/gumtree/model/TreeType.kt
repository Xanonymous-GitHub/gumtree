package tw.xcc.gumtree.model

/**
 * TreeType corresponds to the name of their production rule in the grammar of an AST.
 * e.g. `IfStatement`, `FunctionStatement`
 * */
data class TreeType(val name: String) {
    fun isEmpty(): Boolean = name.isEmpty()

    companion object {
        fun empty(): TreeType = TreeType("")
    }
}