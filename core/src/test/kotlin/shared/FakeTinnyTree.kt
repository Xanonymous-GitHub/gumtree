package shared

import org.jetbrains.annotations.TestOnly
import tw.xcc.gumtree.model.BasicTree

@TestOnly
internal class FakeTinnyTree : BasicTree<FakeTinnyTree>() {
    override val self = this

    override fun similarityProperties(): String = throw NotImplementedError()
}