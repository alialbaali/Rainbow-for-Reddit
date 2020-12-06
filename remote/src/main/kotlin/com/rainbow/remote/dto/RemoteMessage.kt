package com.rainbow.remote.dto

data class RemoteMessage(
    val id: String?,
    val subject: String?,
    val text: String?,
    val to: String?,
    val fromSr: String?,
    val firstMessage: Long?,
    val firstMessageName: String?,
    val subreddit: String?,
    val authorFullname: String?,
    val author: String?,
    val score: Long?,
)