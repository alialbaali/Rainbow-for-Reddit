package com.rainbow.data.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Year

internal fun Long?.toSystemLocalDateTime(): LocalDateTime {
    return this?.let {
        Instant.fromEpochSeconds(it)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    } ?: ZeroDateTime
}

val ZeroDateTime = LocalDateTime(Year.MIN_VALUE, 1, 1, 1, 1, 1, 1)