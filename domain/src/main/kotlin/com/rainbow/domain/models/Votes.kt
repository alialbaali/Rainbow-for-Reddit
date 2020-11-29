package com.rainbow.domain.models


data class Votes(
    val upvotesCount: Long,
    val downvotesCount: Long,
    val voteRatio: Float,
)