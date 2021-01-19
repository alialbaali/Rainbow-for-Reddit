package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMod(
    val id: String,

    val name: String,

    @SerialName("mod_permissions")
    val modPermissions: List<ModPermission>,

    val date: Double,
)

@Serializable
enum class ModPermission {
    @SerialName("wiki")
    Wiki,

    @SerialName("posts")
    Posts,

    @SerialName("mail")
    Mail,

    @SerialName("config")
    Config,

    @SerialName("flair")
    Flair,

    @SerialName("access")
    Access,

    @SerialName("all")
    All;

    companion object {
        val All = listOf(Wiki, Posts, Mail, Config, Flair, Access)
    }
}