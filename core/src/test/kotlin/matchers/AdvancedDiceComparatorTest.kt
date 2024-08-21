package matchers

import helpers.gumTree
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.matchers.GreedyTopDownMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.test.BeforeTest
import kotlin.test.Test

@Execution(ExecutionMode.CONCURRENT)
class AdvancedDiceComparatorTest {
    private lateinit var subTree1: GumTree
    private lateinit var subTree2: GumTree
    private lateinit var diceComparator: Any
    private lateinit var diceComparatorCompareFunc: KFunction<*>

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

        val storage = MappingStorage()
        val topDownMatcher = GreedyTopDownMatcher()
        diceComparator =
            topDownMatcher::class
                .nestedClasses
                .first { it.simpleName == "AdvancedDiceComparator" }
                .primaryConstructor!!
                .apply { isAccessible = true }
                .call(topDownMatcher, storage)

        diceComparatorCompareFunc =
            diceComparator::class.declaredMemberFunctions
                .first { it.name == "compare" }
                .apply { isAccessible = true }
    }

    @Test
    fun `test dice comparator`() {
        diceComparatorCompareFunc
            .call(
                diceComparator,
                subTree1 to subTree2,
                subTree1.getChildren().first() to subTree2.getChildren().first()
            )
    }

    @Test
    fun `test dice comparator with same parent`() {
        diceComparatorCompareFunc
            .call(
                diceComparator,
                gumTree("m1.1") to gumTree("m2.1"),
                gumTree("m1.2") to gumTree("m2.2")
            )
    }

    @Test
    fun `test dice comparator with different parent but one of them is null`() {
        val sameParentTree =
            gumTree("sameParent") {
                child("m1.1")
                child("m1.2")
            }

        diceComparatorCompareFunc
            .call(
                diceComparator,
                gumTree("m2.1") to sameParentTree.getChildren().first(),
                sameParentTree.getChildren().last() to gumTree("m2.2")
            )

        diceComparatorCompareFunc
            .call(
                diceComparator,
                sameParentTree.getChildren().last() to gumTree("m2.2"),
                gumTree("m2.1") to sameParentTree.getChildren().first()
            )
    }
}