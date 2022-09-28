package com.rainbow.remote.source

import com.rainbow.remote.dto.RemotePost

interface RemotePostDataSource {

    suspend fun getHomePosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getPopularPosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getAllPosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getUserUpvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getUserDownvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getUserHiddenPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

    suspend fun getPost(postId: String): RemotePost

    suspend fun followPost(postId: String): String

    suspend fun unFollowPost(postId: String): String

    suspend fun upvotePost(postId: String)

    suspend fun unvotePost(postId: String)

    suspend fun downvotePost(postId: String)

    suspend fun savePost(postId: String)

    suspend fun unSavePost(postId: String)

    suspend fun hidePost(postId: String)

    suspend fun unHidePost(postId: String)

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

    suspend fun searchPosts(
        searchTerm: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost>

}