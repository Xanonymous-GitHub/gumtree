package tw.xcc.gumtree.helper

import tw.xcc.gumtree.model.GumTree

fun isIsomorphicTo(
    self: GumTree,
    other: GumTree
): Boolean =
    compareTrees(self, other, true) { child, otherChild ->
        child isIsomorphicTo otherChild
    }

fun isIsoStructuralTo(
    self: GumTree,
    other: GumTree
): Boolean =
    compareTrees(self, other, false) { child, otherChild ->
        child isIsoStructuralTo otherChild
    }

private inline fun compareTrees(
    self: GumTree,
    other: GumTree,
    shouldCheckLabel: Boolean,
    childComparison: (GumTree, GumTree) -> Boolean
): Boolean {
    synchronized(self) {
        if (!(self hasSameTypeAs other && self.childCount() == other.childCount())) {
            return false
        }

        if (shouldCheckLabel && !(self hasSameLabelAs other)) {
            return false
        }

        for (i in 0 until self.childCount()) {
            val child = self.childAt(i) ?: return false
            val otherChild = other.childAt(i) ?: return false
            if (!childComparison(child, otherChild)) {
                return false
            }
        }

        return true
    }
}