package com.rainbow.remote.source

import com.rainbow.remote.dto.RemotePost

interface RemotePostDataSource {

    suspend fun getMainPagePosts(
        mainPageSorting: String,
        timeSorting: String,
        lastPostIdPrefixed: String?,
    ): Result<List<RemotePost>>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
    ): Result<List<RemotePost>>

    suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
    ): Result<List<RemotePost>>

    suspend fun upvotePost(postIdPrefixed: String): Result<Unit>

    suspend fun unvotePost(postIdPrefixed: String): Result<Unit>

    suspend fun downvotePost(postIdPrefixed: String): Result<Unit>

    suspend fun savePost(postIdPrefixed: String): Result<Unit>

    suspend fun unSavePost(postIdPrefixed: String): Result<Unit>

    suspend fun hidePost(postIdPrefixed: String): Result<Unit>

    suspend fun unHidePost(postIdPrefixed: String): Result<Unit>

    suspend fun submitTextPost(
        subredditName: String,
        title: String,
        text: String?,
        isNsfw: Boolean = false,
        isSpoiler: Boolean = false,
        resubmit: Boolean = true,
    ): Result<Unit>

    suspend fun submitUrlPost(
        subredditName: String,
        title: String,
        url: String,
        isNsfw: Boolean = false,
        isSpoiler: Boolean = false,
        resubmit: Boolean = true,
    ): Result<Unit>

    suspend fun deletePost(postIdPrefixed: String): Result<Unit>

}