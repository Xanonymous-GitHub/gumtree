package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.PriorityTreeList

class HeightPriorityList : PriorityTreeList<GumTree> {
    private val trees = sortedMapOf<Int, MutableList<GumTree>>()

    override fun push(tree: GumTree) {
        synchronized(this) {
            val height = tree.height
            if (trees.containsKey(height)) {
                trees[height]!!.add(tree)
            } else {
                trees[height] = mutableListOf(tree)
            }
        }
    }

    override fun open(tree: GumTree) =
        synchronized(this) {
            tree.getChildren().forEach { push(it) }
        }

    override fun pop(): List<GumTree> =
        synchronized(this) {
            val max = peekMax()
            val result = trees[max]!!
            trees.remove(max)
            return result
        }

    override fun popOpen(): List<GumTree> =
        synchronized(this) {
            val result = pop()
            result.forEach { open(it) }
            return result
        }

    override fun peekMax(): Int =
        synchronized(this) {
            return trees.keys.maxOrNull()
                ?: throw NoSuchElementException("The list is empty, so there is no maximum value.")
        }

    override fun isEmpty(): Boolean =
        synchronized(this) {
            return trees.isEmpty()
        }

    override fun clear() =
        synchronized(this) {
            trees.clear()
        }
}