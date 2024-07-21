package tw.xcc.gumtree.helper

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import tw.xcc.gumtree.model.GumTree

fun isIsomorphicTo(
    self: GumTree,
    other: GumTree
): Boolean =
    runBlocking {
        compareTrees(self, other, true)
    }

fun isIsoStructuralTo(
    self: GumTree,
    other: GumTree
): Boolean =
    runBlocking {
        compareTrees(self, other, false)
    }

private suspend fun compareTrees(
    self: GumTree,
    other: GumTree,
    shouldCheckLabel: Boolean
): Boolean =
    coroutineScope {
        val selfChildCount = self.childCount()
        if (!(self hasSameTypeAs other && selfChildCount == other.childCount())) {
            return@coroutineScope false
        }

        if (shouldCheckLabel && !(self hasSameLabelAs other)) {
            return@coroutineScope false
        }

        if (selfChildCount == 0) {
            return@coroutineScope true
        }

        val selfChildren = self.getChildren()
        val otherChildren = other.getChildren()

        val jobs = mutableListOf<Deferred<Boolean>>()
        selfChildren.zip(otherChildren).forEach { (selfChild, otherChild) ->
            val job =
                async(Dispatchers.Default) {
                    println(coroutineContext)
                    ensureActive()
                    compareTrees(selfChild, otherChild, shouldCheckLabel)
                }
            jobs.add(job)
        }

        jobs.forEach {
            if (!it.await()) {
                return@coroutineScope false
            }
        }

        return@coroutineScope true
    }