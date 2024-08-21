package matchers

import helpers.gumTree
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.comparator.SubtreeSizeComparator
import tw.xcc.gumtree.model.GumTree
import kotlin.test.BeforeTest
import kotlin.test.Test

@Execution(ExecutionMode.CONCURRENT)
class SubtreeSizeComparatorTest {
    private lateinit var subTree1: GumTree
    private lateinit var subTree2: GumTree
    private lateinit var comparator: SubtreeSizeComparator

    @BeforeTest
    fun setUp() {
        gumTree("m1.1") {
            subTree1 =
                child("m1.2") {
                    child("m1.3")
                }
        }

        gumTree("m2.1") {
            subTree2 =
                child("m2.2") {
                    child("m2.3")
                }
        }
        comparator = SubtreeSizeComparator()
    }

    @Test
    fun `test compare subtrees`() {
        comparator.compare(
            setOf(subTree1) to setOf(subTree2),
            setOf(subTree1.getChildren().first()) to setOf(subTree2.getChildren().first())
        )
    }
}