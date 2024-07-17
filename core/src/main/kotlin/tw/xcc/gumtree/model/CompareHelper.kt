package tw.xcc.gumtree.model

import tw.xcc.gumtree.api.tree.Comparable

class CompareHelper(private val tree: GumTree) : Comparable<GumTree> {
    override fun isIsomorphicTo(other: GumTree): Boolean =
        compareTrees(other, true) { child, otherChild ->
            child isIsomorphicTo otherChild
        }

    override fun isIsoStructuralTo(other: GumTree): Boolean =
        compareTrees(other, false) { child, otherChild ->
            child isIsoStructuralTo otherChild
        }

    private inline fun compareTrees(
        other: GumTree,
        shouldCheckLabel: Boolean,
        childComparison: (GumTree, GumTree) -> Boolean
    ): Boolean {
        synchronized(tree) {
            if (!(tree hasSameTypeAs other && tree.childCount() == other.childCount())) {
                return false
            }

            if (shouldCheckLabel && !(tree hasSameLabelAs other)) {
                return false
            }

            for (i in 0 until tree.childCount()) {
                val child = tree.childAt(i) ?: return false
                val otherChild = other.childAt(i) ?: return false
                if (!childComparison(child, otherChild)) {
                    return false
                }
            }

            return true
        }
    }
}