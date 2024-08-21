package editscript

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import shared.realTree1
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.editscript.SimplifiedEditScriptGenerator
import tw.xcc.gumtree.matchers.UniversalMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class SimplifiedScriptGeneratorTest {
    private lateinit var generator: SimplifiedEditScriptGenerator
    private lateinit var storage: TreeMappingStorage<GumTree>

    @BeforeTest
    fun setUp() {
        storage = MappingStorage()
    }

    @Test
    fun `test generate for real tree 1`() {
        val (tree1, tree2) = realTree1

        val matcher = UniversalMatcher()

        runBlocking {
            matcher.match(tree1, tree2, storage)
            generator = SimplifiedEditScriptGenerator(tree1, tree2, storage)
            val actions = generator.generateActions()
            assertTrue(tree1 isIsomorphicTo tree2)
            assertEquals(7, actions.size)
        }
    }
}