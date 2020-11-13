package com.rainbow.remote.dto

data class RemoteMessage(
    val id: String?,
    val subject: String?,
    val text: String?,
    val to: String?,
    val fromSr: String?,
)