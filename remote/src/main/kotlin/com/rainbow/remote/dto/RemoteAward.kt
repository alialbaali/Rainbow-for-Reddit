package com.rainbow.remote.dto

data class RemoteAward(
    val id: String?,
    val name: String?,
    val staticIconUrl: String?,
    val description: String?,
    val awardSubType: AwardSubType?,
    val awardType: AwardSubType?,
    val count: Int?,
    val coinPrice: Int?,
) {
    enum class AwardSubType {
        Global, Premium,
        Appreciation, Group,
    }
}