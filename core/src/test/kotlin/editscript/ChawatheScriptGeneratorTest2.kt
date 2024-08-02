package editscript

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import shared.realTree1
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.editscript.ChawatheScriptGenerator
import tw.xcc.gumtree.matchers.UniversalMatcher
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class ChawatheScriptGeneratorTest2 {
    private lateinit var generator: ChawatheScriptGenerator
    private lateinit var storage: TreeMappingStorage<GumTree>

    @BeforeTest
    fun setUp() {
        storage = MappingStorage()
    }

    @Test
    fun `test generate for sample tree 1`() {
        val (tree1, tree2) = realTree1

        val matcher = UniversalMatcher()

        runBlocking {
            matcher.match(tree1, tree2, storage)
            generator = ChawatheScriptGenerator(tree1, tree2, storage)
            generator.generateActions()
            assertTrue(tree1 isIsomorphicTo tree2)
        }
    }
}