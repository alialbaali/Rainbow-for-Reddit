package com.rainbow.desktop.utils

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

private const val is24HourFormat = true // TODO

fun Instant.formatDateOnly(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val format = "EEE, d MMM yyyy"

    return toLocalDateTime(timeZone).toJavaLocalDateTime()
        .format(DateTimeFormatter.ofPattern(format))
}

fun Instant.formatDisplayTime(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val thisDateTime = this.toLocalDateTime(timeZone)
    val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)
    val yearDiff = currentDateTime.year - thisDateTime.year
    val monthDiff = currentDateTime.monthNumber - thisDateTime.monthNumber
    val dayDiff = currentDateTime.dayOfYear - thisDateTime.dayOfYear
    val hourDiff = currentDateTime.hour - thisDateTime.hour
    val minuteDiff = currentDateTime.minute - thisDateTime.minute
    if (yearDiff != 0) return RainbowStrings.YearAgo(yearDiff)
    if (monthDiff != 0) return RainbowStrings.MonthAgo(monthDiff)
    if (dayDiff != 0) return RainbowStrings.DayAgo(dayDiff)
    if (hourDiff != 0) return RainbowStrings.HourAgo(hourDiff)
    if (minuteDiff != 0) return RainbowStrings.MinuteAgo(minuteDiff)
    return RainbowStrings.Now
}