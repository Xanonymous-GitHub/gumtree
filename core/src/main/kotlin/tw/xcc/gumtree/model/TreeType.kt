package tw.xcc.gumtree.model

data class TreeType(val name: String) {
    fun isEmpty(): Boolean = name.isEmpty()
}