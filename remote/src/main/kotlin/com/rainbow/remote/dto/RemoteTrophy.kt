package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteTrophy(
    @SerialName("award_id")
    val awardId: String? = null, // null
    @SerialName("description")
    val description: String? = null, // null
    @SerialName("granted_at")
    val grantedAt: Int? = null, // 1603949136
    @SerialName("icon_40")
    val icon40: String? = null, // https://www.redditstatic.com/awards2/1_year_club-40.png
    @SerialName("icon_70")
    val icon70: String? = null, // https://www.redditstatic.com/awards2/1_year_club-70.png
    @SerialName("id")
    val id: String? = null, // null
    @SerialName("name")
    val name: String? = null, // One-Year Club
    @SerialName("url")
    val url: String? = null // null
)