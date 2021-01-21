package com.rainbow.domain.repository

import com.rainbow.domain.models.Subreddit

interface SubredditRepository {

    suspend fun getMySubreddits(): Result<List<Subreddit>>

    suspend fun getSubreddit(subredditName: String): Result<Subreddit>

}