package tw.xcc.gumtree.helper

import com.appmattus.crypto.Algorithm.XXH3_64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import tw.xcc.gumtree.model.BasicTree
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

private const val SEED = 0x0L
private val xxh364 = XXH3_64.Seeded(SEED)

fun <T : BasicTree<T>> createHashMemoOf(tree: T): Map<String, Long> {
    val memo = ConcurrentHashMap<String, Long>()
    runBlocking(Dispatchers.Default) { createHashMemoOf(tree, memo) }
    return memo
}

private suspend fun <T : BasicTree<T>> createHashMemoOf(
    tree: T,
    memo: MutableMap<String, Long>
): ByteArray =
    coroutineScope {
        val digest = xxh364.createDigest()

        if (tree.childCount() > 0) {
            tree.getChildren().map { child ->
                async { createHashMemoOf(child, memo) }
            }.awaitAll().forEach { childHash ->
                digest.update(childHash)
            }
        }

        val hash = digest.digest(tree.similarityProperties().toByteArray())
        memo[tree.id] = hash.toLong()
        return@coroutineScope hash
    }

private fun ByteArray.toLong(): Long = ByteBuffer.wrap(this).long