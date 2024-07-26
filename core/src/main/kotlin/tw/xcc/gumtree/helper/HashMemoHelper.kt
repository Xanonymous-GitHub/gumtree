package tw.xcc.gumtree.helper

import com.appmattus.crypto.Algorithm.XXH3_64
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import tw.xcc.gumtree.model.BasicTree
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

private const val SEED = 0x0L
private val xxh364 = XXH3_64.Seeded(SEED)

suspend fun <T : BasicTree<T>> createHashMemoOf(tree: T): Map<String, Long> =
    coroutineScope {
        val memo = ConcurrentHashMap<String, Long>()
        createHashMemoOf(tree, memo)
        return@coroutineScope memo
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