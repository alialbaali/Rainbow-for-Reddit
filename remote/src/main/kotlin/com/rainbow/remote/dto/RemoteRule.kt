package com.rainbow.remote.dto

data class RemoteRule(
    val kind: String?,
    val shortName: String?,
    val description: String?,
    val violationReason: String?,
    val createdUtc: Long?,
    val priority: Long?,
)
