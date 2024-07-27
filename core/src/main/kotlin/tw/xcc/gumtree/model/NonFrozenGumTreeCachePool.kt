package tw.xcc.gumtree.model

import java.util.concurrent.ConcurrentHashMap

/**
 * An additional thread-safe cache pool for non-frozen GumTree instances (not [GumTreeView]).
 * The key is the id of the [GumTree] instance.
 * When a [GumTree] instance is put into the pool, it will be checked if it is a [GumTreeView] instance.
 * */
internal object NonFrozenGumTreeCachePool : MutableMap<String, GumTree> {
    private val cache = ConcurrentHashMap<String, GumTree>()

    override val entries: MutableSet<MutableMap.MutableEntry<String, GumTree>>
        get() = cache.entries
    override val keys: MutableSet<String>
        get() = cache.keys
    override val size: Int
        get() = cache.size
    override val values: MutableCollection<GumTree>
        get() = cache.values

    override fun clear() = cache.clear()

    override fun isEmpty(): Boolean = cache.isEmpty()

    override fun remove(key: String): GumTree? = cache.remove(key)

    override fun putAll(from: Map<out String, GumTree>) {
        from.forEach {
            if (it.value is GumTreeView) {
                throw IllegalArgumentException("GumTreeView instances are not allowed to be put into the cache pool.")
            }
        }
        cache.putAll(from)
    }

    override fun put(
        key: String,
        value: GumTree
    ): GumTree? {
        if (value is GumTreeView) {
            throw IllegalArgumentException("GumTreeView instances are not allowed to be put into the cache pool.")
        }
        return cache.put(key, value)
    }

    override fun get(key: String): GumTree? = cache[key]

    override fun containsValue(value: GumTree): Boolean = cache.containsValue(value)

    override fun containsKey(key: String): Boolean = cache.containsKey(key)

    /**
     * A helper function to extract the real [GumTree] instances from a pair of [GumTree] instances.
     * The given [pair] should be [GumTreeView] instances, however, we won't perform any runtime type checking.
     * Because that causes significant performance impact.
     * */
    fun mustExtractRealOf(pair: Pair<GumTree, GumTree>): Pair<GumTree, GumTree> =
        synchronized(this) {
            val first = cache[pair.first.id]
            val second = cache[pair.second.id]

            if (first == null || second == null) {
                throw IllegalArgumentException("At least one of GumTree instances can't be found in the pool.")
            }

            return first to second
        }
}