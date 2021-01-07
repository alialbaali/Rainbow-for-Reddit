package com.rainbow.domain.models

import com.rainbow.domain.utils.OauthUrl
import com.rainbow.domain.utils.asUserDisplayName
import kotlinx.datetime.LocalDateTime

private const val DefaultUserImageUrl = ""

data class User(
    val id: String,
    val name: String,
    val postKarma: Long,
    val commentKarma: Long,
    val imageUrl: String = DefaultUserImageUrl,
    val isNSFW: Boolean = false,
    val creationDate: LocalDateTime,
)

val User.totalKarma
    get() = postKarma + commentKarma

val User.redditUrl
    get() = "/user/$name"

val User.fullUrl
    get() = OauthUrl + redditUrl

val User.displayName
    get() = name.asUserDisplayName()