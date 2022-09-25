package com.rainbow.domain.models

import kotlinx.datetime.Instant

data class Rule(
    val title: String,
    val description: String,
    val priority: Int,
    val violationReason: String,
    val creationDate: Instant,
)