package com.rainbow.local

import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface LocalPostDataSource {

    val posts: Flow<List<Post>>

    val homePosts: Flow<List<Post>>

    val profileSubmittedPosts: Flow<List<Post>>

    val profileUpvotedPosts: Flow<List<Post>>

    val profileDownvotedPosts: Flow<List<Post>>

    val profileHiddenPosts: Flow<List<Post>>

    val userSubmittedPosts: Flow<List<Post>>

    val subredditPosts: Flow<List<Post>>

    val searchPosts: Flow<List<Post>>

    fun insertPost(post: Post)

    fun insertHomePost(post: Post)

    fun insertProfileSubmittedPost(post: Post)

    fun insertProfileUpvotedPost(post: Post)

    fun insertProfileDownvotedPost(post: Post)

    fun insertProfileHiddenPost(post: Post)

    fun insertUserSubmittedPost(post: Post)

    fun insertSubredditPost(post: Post)

    fun insertSearchPost(post: Post)

    fun getPost(postId: String): Flow<Post?>

    fun updatePost(postId: String, block: (Post) -> Post)

    fun clearPosts()

    fun clearHomePosts()

    fun clearProfileSubmittedPosts()

    fun clearProfileUpvotedPosts()

    fun clearProfileDownvotedPosts()

    fun clearProfileHiddenPosts()

    fun clearUserSubmittedPosts()

    fun clearSubredditPosts()

    fun clearSearchPosts()

}