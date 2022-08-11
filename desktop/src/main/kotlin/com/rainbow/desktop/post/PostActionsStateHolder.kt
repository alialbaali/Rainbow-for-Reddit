package com.rainbow.desktop.post

import com.rainbow.data.Repos
import com.rainbow.desktop.model.StateHolder
import com.rainbow.domain.models.Post
import kotlinx.coroutines.launch

object PostActionsStateHolder : StateHolder() {

    fun upvotePost(post: Post) = scope.launch {
        Repos.Post.upvotePost(post.id)
    }

    fun downvotePost(post: Post) = scope.launch {
        Repos.Post.downvotePost(post.id)
    }

    fun unvotePost(post: Post) = scope.launch {
        Repos.Post.unvotePost(post.id)
    }

    fun hidePost(post: Post) = scope.launch {
        Repos.Post.hidePost(post.id)
    }

    fun unHidePost(post: Post) = scope.launch {
        Repos.Post.unHidePost(post.id)
    }

    fun readPost(post: Post) {

    }

}