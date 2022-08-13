package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val posts: Flow<List<Post>>

    suspend fun getCurrentUserSubmittedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>>

    suspend fun getCurrentUserUpvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>>

    suspend fun getCurrentUserDownvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>>

    suspend fun getCurrentUserHiddenPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>>

    suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    suspend fun getHomePosts(
        postsSorting: HomePostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: SubredditPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    fun getPost(postId: String): Flow<Result<Post>>

    suspend fun createPost(post: Post): Result<Unit>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun upvotePost(postId: String): Result<Unit>

    suspend fun unvotePost(postId: String): Result<Unit>

    suspend fun downvotePost(postId: String): Result<Unit>

    suspend fun hidePost(postId: String): Result<Unit>

    suspend fun unHidePost(postId: String): Result<Unit>

    suspend fun searchPosts(
        searchTerm: String,
        postSorting: SearchPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>>
}