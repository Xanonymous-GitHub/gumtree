package tw.xcc.gumtree.api

/**
 * A base class to facilitate [operator ==] and [hashCode] overrides.
 * */
abstract class Equatable {
    /**
     * The list of properties that will be used to determine whether two instances are equal.
     * */
    protected abstract val equatableProps: List<Any?>

    final override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Equatable) return false
        if (equatableProps.size != other.equatableProps.size) return false
        for (i in equatableProps.indices) {
            if (equatableProps[i] != other.equatableProps[i]) return false
        }
        return true
    }

    final override fun hashCode(): Int = equatableProps.hashCode() xor javaClass.hashCode()
}