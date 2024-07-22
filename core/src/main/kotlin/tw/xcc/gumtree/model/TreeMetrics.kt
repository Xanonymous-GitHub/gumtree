package tw.xcc.gumtree.model

data class TreeMetrics(
    val height: Int,
    val depth: Int,
    val subTreeSize: Int
) {
    companion object {
        fun empty(): TreeMetrics = TreeMetrics(-1, -1, -1)
    }
}