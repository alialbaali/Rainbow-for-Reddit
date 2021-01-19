package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemoteAward(
    val id: String,

    val name: String,

    val description: String,

    val count: Long,

    @SerialName("coin_price")
    val coinPrice: Long,

    @SerialName("award_sub_type")
    val awardSubType: AwardSubType,

    @SerialName("icon_url")
    val IconUrl: String,

    @SerialName("static_icon_url")
    val staticIconUrl: String,
) {
    @Serializable
    enum class AwardSubType {
        @SerialName("GLOBAL")
        Global,

        @SerialName("PREMIUM")
        Premium,

        @SerialName("APPRECIATION")
        Appreciation,

        @SerialName("GROUP")
        Group,

        @SerialName("COMMUNITY")
        Community,
    }
}