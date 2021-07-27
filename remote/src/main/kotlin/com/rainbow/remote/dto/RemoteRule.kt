package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteRule(
    val kind: String?,
    @SerialName("short_name")
    val shortName: String?,
    val description: String?,
    @SerialName("violation_reason")
    val violationReason: String?,
    @SerialName("created_utc")
    val createdUtc: Double,
    val priority: Long?,
)
