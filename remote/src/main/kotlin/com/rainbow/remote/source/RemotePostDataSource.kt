package com.rainbow.remote.source

import com.rainbow.remote.Listing
import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemotePost

interface RemotePostDataSource {

    suspend fun getMainPagePosts(
        mainPageSorting: String,
        timeSorting: String,
        lastPostIdPrefixed: String?,
    ): RedditResponse<Listing<RemotePost>>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String
    ): RedditResponse<Listing<RemotePost>>

    suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>>

    suspend fun upvotePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun unvotePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun downvotePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun savePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun unSavePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun hidePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun unHidePost(postIdPrefixed: String): RedditResponse<Unit>

    suspend fun submitPost(remotePost: RemotePost): RedditResponse<Unit>

}