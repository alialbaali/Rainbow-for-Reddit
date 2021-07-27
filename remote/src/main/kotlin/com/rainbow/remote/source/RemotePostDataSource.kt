package com.rainbow.remote.source

import com.rainbow.remote.dto.RemotePost

interface RemotePostDataSource {

    suspend fun getHomePosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>>

    suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>>

    suspend fun getPost(postId: String): Result<RemotePost>

    suspend fun followPost(postId: String): String

    suspend fun unFollowPost(postId: String): String

    suspend fun upvotePost(postId: String): Result<Unit>

    suspend fun unvotePost(postId: String): Result<Unit>

    suspend fun downvotePost(postId: String): Result<Unit>

    suspend fun savePost(postId: String): Result<Unit>

    suspend fun unSavePost(postId: String): Result<Unit>

    suspend fun hidePost(postId: String): Result<Unit>

    suspend fun unHidePost(postId: String): Result<Unit>

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

    suspend fun deletePost(postId: String): Result<Unit>

}