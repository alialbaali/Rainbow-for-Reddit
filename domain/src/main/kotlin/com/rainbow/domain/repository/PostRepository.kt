package com.rainbow.domain.repository

import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostsSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getMyPosts(
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>>

    fun getHomePosts(
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>>

    fun getSubredditPosts(
        subredditName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>>

    fun getUserPosts(
        userName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>>

    fun getPost(postId: String): Flow<Result<Post>>

    suspend fun createPost(post: Post): Result<Unit>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun upvotePost(postId: String): Result<Unit>

    suspend fun unvotePost(postId: String): Result<Unit>

    suspend fun downvotePost(postId: String): Result<Unit>

}