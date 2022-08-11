package com.rainbow.local

import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface LocalPostDataSource {

    val posts: Flow<List<Post>>

    fun insertPost(post: Post)

    fun upvotePost(postId: String)

    fun downvotePost(postId: String)

    fun unvotePost(postId: String)

    fun clearPosts()

}