import tw.xcc.gumtree.model.BasicTree

internal class FakeTinnyTree : BasicTree<FakeTinnyTree>() {
    override val self = this

    override fun similarityProperties(): String = throw NotImplementedError()
}