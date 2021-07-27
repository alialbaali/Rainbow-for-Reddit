package com.rainbow.remote.dto

internal enum class Scope {
    ModPosts, ModWiki, MySubreddits,
    PrivateMessages, Read, Report,
    History, ModConfig, ModFlair,
    Vote, WikiEdit, WikiRead,
    Save, Submit, Subscribe,
    Identity, Edit, Flair;

    companion object {

        val UserScope = listOf(
            PrivateMessages, Subscribe,
            Submit, Vote, MySubreddits,
            History, WikiRead, Flair,
            Identity, Edit, Read,
            Report, Save, Submit,
        )

        val ModScope = listOf(ModConfig, ModFlair, ModPosts, ModWiki, WikiEdit)

        val All = listOf(
            ModPosts, ModWiki, WikiEdit,
            Submit, Vote, MySubreddits,
            PrivateMessages, Subscribe,
            History, WikiRead, Flair,
            Identity, Edit, Read,
            Report, Save, Submit,
            ModConfig, ModFlair,
        )

        fun List<Scope>.joinToLowerCaseString() = joinToString(separator = " ") { scope -> scope.name.lowercase() }

    }

}