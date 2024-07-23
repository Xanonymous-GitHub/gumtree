package tw.xcc.gumtree.helper

import org.lwjgl.util.xxhash.XXHash
import java.nio.ByteBuffer

/**
 * A helper function to calculate the fixed seed hash of a [String].
 * It uses the XXH3-64 algorithm to calculate the hash.
 * */
internal fun fixedSeedHashOf(thing: String): Long {
    val byteBuffer = ByteBuffer.wrap(thing.toByteArray())
    return XXHash.XXH3_64bits(byteBuffer)
}