package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteModerator(
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String? = null, // fortegreen
    @SerialName("author_flair_text")
    val authorFlairText: String? = null, // Moderator
    @SerialName("date")
    val date: Double? = null, // 1608064235.0
    @SerialName("id")
    val id: String? = null, // t2_87fb8oh0
    @SerialName("mod_permissions")
    val modPermissions: List<String>? = null,
    @SerialName("name")
    val name: String? = null, // PlantainTop
    @SerialName("rel_id")
    val relId: String? = null // rb_2e6t0s7
)