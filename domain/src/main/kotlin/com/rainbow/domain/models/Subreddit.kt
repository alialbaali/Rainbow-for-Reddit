package com.rainbow.domain.models

import com.rainbow.domain.colorOf
import kotlinx.datetime.LocalDate

private val DefaultPrimaryColor = colorOf(red = "00", green = "00", blue = "00")
private val DefaultBackgroundColor = colorOf(red = "FF", green = "FF", blue = "FF")
private val DefaultKeyColor = colorOf(red = "FF", green = "FF", blue = "FF")

data class Subreddit(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val subscribersCount: Long,
    val activeSubscribersCount: Long,
    val colors: Colors = DefaultColors,
    val isNSFW: Boolean = false,
    val creationDate: LocalDate,
) {
    data class Colors(
        val primary: Long,
        val background: Long,
        val key: Long,
    )
}

private val DefaultColors = Subreddit.Colors(DefaultPrimaryColor, DefaultBackgroundColor, DefaultKeyColor)

val Subreddit.idPrefixed
    get() = "t5_$id"

val Subreddit.namePrefixed
    get() = "r/$name"

val Subreddit.redditUrl
    get() = "/r/$name"

val Subreddit.primaryColor
    get() = colors.primary

val Subreddit.backgroundColor
    get() = colors.background

val Subreddit.keyColor
    get() = colors.key