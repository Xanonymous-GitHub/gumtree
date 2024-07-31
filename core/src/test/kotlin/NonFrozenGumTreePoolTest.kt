import helpers.gumTree
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.model.NonFrozenGumTreeCachePool
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@Execution(ExecutionMode.SAME_THREAD)
internal class NonFrozenGumTreePoolTest {
    private val givenPool = NonFrozenGumTreeCachePool

    @BeforeTest
    fun setUp() {
        givenPool.clear()
    }

    @Test
    fun `test putAll`() {
        val origin =
            gumTree("root") {
                child("child")
            }

        givenPool.putAll(origin.preOrdered().associateBy { it.id })
        assertEquals(2, givenPool.size)

        val extracted = givenPool[origin.id]
        assertSame(extracted, origin)
    }

    @Test
    fun `test put`() {
        val origin =
            gumTree("root") {
                child("child")
            }

        givenPool[origin.id] = origin
        assertEquals(1, givenPool.size)

        val extracted = givenPool[origin.id]
        assertSame(extracted, origin)
    }

    @Test
    fun `test putAll with GumTreeView`() {
        val origin =
            gumTree("root") {
                child("child")
            }.toNewFrozen()

        assertThrows<IllegalArgumentException> {
            givenPool.putAll(origin.preOrdered().associateBy { it.id })
        }
    }

    @Test
    fun `test put with GumTreeView`() {
        val origin =
            gumTree("root") {
                child("child")
            }.toNewFrozen()

        assertThrows<IllegalArgumentException> {
            givenPool[origin.id] = origin
        }
    }

    @Test
    fun `test mustExtractRealOf`() {
        val originL =
            gumTree("rootL") {
                child("childL")
            }
        val originR =
            gumTree("rootR") {
                child("childR")
            }

        givenPool.putAll(originL.preOrdered().associateBy { it.id })
        givenPool.putAll(originR.preOrdered().associateBy { it.id })

        val pair = originL to originR
        val extracted = givenPool.mustExtractRealOf(pair)

        assertSame(originL, extracted.first)
        assertSame(originR, extracted.second)
    }

    @Test
    fun `test tryExtractRealOf`() {
        val originL =
            gumTree("rootL") {
                child("childL")
            }
        val originR =
            gumTree("rootR") {
                child("childR")
            }

        givenPool.putAll(originL.preOrdered().associateBy { it.id })
        givenPool.putAll(originR.preOrdered().associateBy { it.id })

        val pair = originL to originR
        val extracted = givenPool.tryExtractRealOf(pair)

        assertSame(originL, extracted.first)
        assertSame(originR, extracted.second)
    }

    @Test
    fun `test tryExtractRealOf with missing instances`() {
        val originL =
            gumTree("rootL") {
                child("childL")
            }
        val originR =
            gumTree("rootR") {
                child("childR")
            }

        givenPool.putAll(originL.preOrdered().associateBy { it.id })

        val pair = originL to originR
        val extracted = givenPool.tryExtractRealOf(pair)

        assertSame(originL, extracted.first)
        assertSame(originR, extracted.second)
    }
}