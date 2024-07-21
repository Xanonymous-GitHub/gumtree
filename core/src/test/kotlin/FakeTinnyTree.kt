import tw.xcc.gumtree.model.BasicTree

internal class FakeTinnyTree : BasicTree<FakeTinnyTree>() {
    override val self = this

    override fun similarityHashCode() = throw NotImplementedError()

    override fun similarityStructureHashCode() = throw NotImplementedError()
}