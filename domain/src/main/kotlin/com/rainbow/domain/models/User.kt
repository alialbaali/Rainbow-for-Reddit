package com.rainbow.domain.models

import kotlinx.datetime.LocalDate

private const val DefaultUserImageUrl = ""

data class User(
    val id: String,
    val name: String,
    val postKarma: Long,
    val commentKarma: Long,
    val imageUrl: String = DefaultUserImageUrl,
    val isNSFW: Boolean = false,
    val creationDate: LocalDate,
)

val User.totalKarma
    get() = postKarma + commentKarma

val User.redditUrl
    get() = "/user/$name"

val User.redditName
    get() = "u/$name"

val User.redditId
    get() = "t2_$id"