package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMessage(
    val id: String,

    val subject: String,

    @SerialName("first-message")
    val firstMessage: Long?,

    @SerialName("first-message_name")
    val firstMessageName: String?,

    val subreddit: String?,

    @SerialName("author_fullname")
    val authorFullname: String?,

    val author: String,

    val score: Long,

    val created: Double,

    @SerialName("created_utc")
    val createdUtc: Double,
)