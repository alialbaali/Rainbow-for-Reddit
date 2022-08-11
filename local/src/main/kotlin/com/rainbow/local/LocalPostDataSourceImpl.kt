package com.rainbow.local

import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalPostDataSourceImpl : LocalPostDataSource {

    private val mutablePosts = MutableStateFlow(emptyList<Post>())
    override val posts get() = mutablePosts.asStateFlow()

    override fun insertPost(post: Post) {
        mutablePosts.value = posts.value + post
    }

    override fun upvotePost(postId: String) {
        mutablePosts.value = posts.value.map { post ->
            if (post.id == postId)
                post.copy(vote = Vote.Up)
            else
                post
        }
    }

    override fun downvotePost(postId: String) {
        mutablePosts.value = posts.value.map { post ->
            if (post.id == postId)
                post.copy(vote = Vote.Down)
            else
                post
        }
    }

    override fun unvotePost(postId: String) {
        mutablePosts.value = posts.value.map { post ->
            if (post.id == postId)
                post.copy(vote = Vote.None)
            else
                post
        }
    }

    override fun clearPosts() {
        mutablePosts.value = emptyList()
    }

}