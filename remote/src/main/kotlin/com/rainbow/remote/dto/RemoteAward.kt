package com.rainbow.remote.dto

data class RemoteAward(
    val id: String,
    val name: String,
    val description: String,
    val count: Long?,
    val coinPrice: Long?, // Might be Double
    val awardSubType: AwardSubType?,
    val awardType: AwardSubType?,
    val IconUrl: String?,
    val staticIconUrl: String,
) {
    enum class AwardSubType {
        Global, Premium,
        Appreciation, Group,
    }
}