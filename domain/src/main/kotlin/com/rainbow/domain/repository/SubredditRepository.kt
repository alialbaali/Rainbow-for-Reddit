package com.rainbow.domain.repository

import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting

interface SubredditRepository {

    suspend fun getMySubreddits(): Result<List<Subreddit>>

    suspend fun getSubreddit(subredditName: String): Result<Subreddit>

    suspend fun subscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<String>

    suspend fun searchSubreddit(subredditName: String, sorting: SubredditsSearchSorting): Result<List<Subreddit>>

}