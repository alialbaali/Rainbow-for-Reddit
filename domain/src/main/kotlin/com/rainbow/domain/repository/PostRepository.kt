package com.rainbow.domain.repository

import com.rainbow.domain.models.MainPage
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostsSorting
import com.rainbow.domain.models.TimeSorting

interface PostRepository {

    suspend fun getMyPosts(
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>>

    suspend fun getMainPagePosts(
        mainPage: MainPage,
        timeSorting: TimeSorting,
        isNext: Boolean = false,
    ): Result<List<Post>>

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>>

    suspend fun getUserPosts(
        userName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>>

    suspend fun createPost(post: Post): Result<Post>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun upvotePost(postId: String): Result<Unit>

    suspend fun unvotePost(postId: String): Result<Unit>

    suspend fun downvotePost(postId: String): Result<Unit>

}