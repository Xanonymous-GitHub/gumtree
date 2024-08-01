package editscript

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import tw.xcc.gumtree.api.tree.TreeMappingStorage
import tw.xcc.gumtree.editscript.ChawatheScriptGenerator
import tw.xcc.gumtree.model.GumTree
import tw.xcc.gumtree.model.MappingStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@Execution(ExecutionMode.CONCURRENT)
internal class ChawatheScriptGeneratorTest1 {
    private lateinit var generator: ChawatheScriptGenerator
    private lateinit var storage: TreeMappingStorage<GumTree>

    @BeforeTest
    fun setUp() {
        storage = MappingStorage()
    }

    private fun addMappingsTo(
        storage: TreeMappingStorage<GumTree>,
        tree1Nodes: List<GumTree>,
        tree2Nodes: List<GumTree>,
        mappingIdxes: Set<Pair<Int, Int>>
    ) = mappingIdxes.forEach { (i, j) ->
        storage.addMappingOf(tree1Nodes[i] to tree2Nodes[j])
    }

    @Test
    fun `test generate for sample tree 1`() {
        val (tree1, tree2) = sampleTree1
        val tree1Nodes = tree1.preOrdered()
        val tree2Nodes = tree2.preOrdered()

        addMappingsTo(
            storage,
            tree1Nodes,
            tree2Nodes,
            setOf(
                0 to 0,
                1 to 1,
                2 to 3,
                3 to 4,
                4 to 2
            )
        )

        generator = ChawatheScriptGenerator(tree1, tree2, storage)

        runBlocking {
            generator.generateActions()
            assertTrue(tree1 isIsomorphicTo tree2)
            assertTrue(tree1 isIsoStructuralTo tree2)
        }
    }

    @Test
    fun `test generate for sample tree `() {
        val (tree1, tree2) = sampleTree2
        val tree1Nodes = tree1.preOrdered()
        val tree2Nodes = tree2.preOrdered()

        addMappingsTo(
            storage,
            tree1Nodes,
            tree2Nodes,
            setOf(
                0 to 0,
                2 to 1,
                3 to 2,
                4 to 7,
                5 to 8,
                6 to 9,
                7 to 3,
                8 to 4,
                9 to 5
            )
        )

        generator = ChawatheScriptGenerator(tree1, tree2, storage)

        runBlocking {
            generator.generateActions()
            assertTrue(tree1 isIsomorphicTo tree2)
            assertTrue(tree1 isIsoStructuralTo tree2)
        }
    }
}