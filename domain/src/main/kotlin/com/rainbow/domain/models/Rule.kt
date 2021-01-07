package com.rainbow.domain.models

import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalUnsignedTypes::class)
data class Rule(
    val title: String,
    val description: String,
    val priority: UInt,
    val violationReason: String,
    val creationDate: LocalDateTime,
)