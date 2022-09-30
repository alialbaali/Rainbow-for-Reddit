package com.rainbow.domain.models

import com.rainbow.domain.utils.RedditUrl
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val DefaultUserImageUrl = ""

data class User(
    val id: String,
    val name: String,
    val description: String?,
    val postKarma: Long,
    val commentKarma: Long,
    val awardeeKarma: Long,
    val awarderKarma: Long,
    val imageUrl: String? = DefaultUserImageUrl,
    val bannerImageUrl: String?,
    val isNSFW: Boolean = false,
    val creationDate: Instant,
)

val User.totalKarma
    get() = postKarma + commentKarma

val User.redditUrl
    get() = "/user/$name"

val User.fullUrl
    get() = RedditUrl + redditUrl

val User.displayName
    get() = name.asUserDisplayName()

val User.isCakeDay: Boolean
    get() {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val date = creationDate.toLocalDateTime(timeZone).date
        return today.dayOfYear == date.dayOfYear
    }