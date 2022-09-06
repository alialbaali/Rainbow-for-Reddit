package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface SubredditRepository {

    val profileSubreddits: Flow<List<Subreddit>>

    val searchSubreddits: Flow<List<Subreddit>>

    suspend fun getProfileSubreddits(lastSubredditId: String?): Result<Unit>

    fun getSubreddit(subredditName: String): Flow<Result<Subreddit>>

    suspend fun getSubredditModerators(subredditName: String): Result<List<Moderator>>

    suspend fun getSubredditRules(subredditName: String): Result<List<Rule>>

    suspend fun subscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit>

    suspend fun favoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit>

    suspend fun getSubredditSubmitText(subredditName: String): Result<String>

    suspend fun getSubredditPostRequirements(subredditName: String): Result<String>

    suspend fun getWikiIndex(subredditName: String): Result<WikiPage>

    suspend fun getWikiPage(subredditName: String, pageName: String): Result<WikiPage>

    suspend fun searchSubreddits(subredditName: String, lastSubredditId: String?): Result<Unit>

    suspend fun getSubredditFlairs(subredditName: String): Result<List<Flair>>

    suspend fun getCurrentSubredditFlair(subredditName: String): Result<Flair>

    suspend fun selectFlair(subredditName: String, flairId: String): Result<Unit>

    suspend fun unselectFlair(subredditName: String): Result<Unit>

}