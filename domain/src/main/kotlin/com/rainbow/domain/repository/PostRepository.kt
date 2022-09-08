package com.rainbow.domain.repository

import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val posts: Flow<List<Post>>

    val homePosts: Flow<List<Post>>

    val profileSubmittedPosts: Flow<List<Post>>

    val profileUpvotedPosts: Flow<List<Post>>

    val profileDownvotedPosts: Flow<List<Post>>

    val profileHiddenPosts: Flow<List<Post>>

    val userSubmittedPosts: Flow<List<Post>>

    val subredditPosts: Flow<List<Post>>

    val searchPosts: Flow<List<Post>>

    suspend fun getProfileSubmittedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    suspend fun getProfileUpvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    suspend fun getProfileDownvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

    suspend fun getProfileHiddenPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>

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

    suspend fun savePost(postId: String): Result<Unit>

    suspend fun unSavePost(postId: String): Result<Unit>

    suspend fun searchPosts(
        searchTerm: String,
        postSorting: SearchPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit>
}