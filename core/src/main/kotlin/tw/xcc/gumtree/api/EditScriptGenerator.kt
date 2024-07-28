package tw.xcc.gumtree.api

import tw.xcc.gumtree.model.operations.Action

internal interface EditScriptGenerator {
    suspend fun generateActions(): List<Action>
}