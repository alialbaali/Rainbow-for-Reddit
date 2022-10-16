package com.rainbow.domain.models

data class Karma(
    val subredditName: String,
    val postKarma: Int,
    val commentKarma: Int,
)