package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface SubredditRepository {

    fun getMySubreddits(lastSubredditId: String?): Flow<Result<List<Subreddit>>>

    fun getSubreddit(subredditName: String): Flow<Result<Subreddit>>

    fun getSubredditModerators(subredditName: String): Flow<Result<List<Moderator>>>

    suspend fun subscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<String>

    suspend fun getWikiIndex(subredditName: String): Result<WikiPage>

    suspend fun getWikiPage(subredditName: String, pageName: String): Result<WikiPage>

    fun searchSubreddit(subredditName: String, sorting: SubredditsSearchSorting): Flow<Result<List<Subreddit>>>

    suspend fun getSubredditFlairs(subredditName: String): Result<List<Flair>>

    suspend fun getCurrentSubredditFlair(subredditName: String): Result<Flair>

    suspend fun selectFlair(subredditName: String, flairId: String): Result<Unit>

    suspend fun unselectFlair(subredditName: String): Result<Unit>

}