package com.rainbow.remote.dto

data class RemoteMod(
    val id: String?,
    val name: String?,
    val modPermissions: List<ModPermission>?,
    val date: Long?,
    val authorFlairText: String?,
)

enum class ModPermission {
    Wiki, Posts, Mail, Config, Flair, Access, All;

    companion object {
        val All = listOf(Wiki, Posts, Mail, Config, Flair, Access)
    }
}