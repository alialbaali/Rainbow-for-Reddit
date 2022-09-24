package com.rainbow.desktop.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

private const val is24HourFormat = true // TODO

fun Instant.formatDateOnly(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val format = "EEE, d MMM yyyy"

    return toLocalDateTime(timeZone).toJavaLocalDateTime()
        .format(DateTimeFormatter.ofPattern(format))
}