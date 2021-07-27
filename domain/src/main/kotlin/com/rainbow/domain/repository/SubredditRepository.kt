package com.rainbow.domain.repository

import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import kotlinx.coroutines.flow.Flow

interface SubredditRepository {

    fun getMySubreddits(lastSubredditId: String?): Flow<Result<List<Subreddit>>>

    fun getSubreddit(subredditName: String): Flow<Result<Subreddit>>

    suspend fun subscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<String>

    fun searchSubreddit(subredditName: String, sorting: SubredditsSearchSorting): Flow<Result<List<Subreddit>>>

}