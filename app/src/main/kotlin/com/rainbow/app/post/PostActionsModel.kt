package com.rainbow.app.post

import com.rainbow.app.model.Model
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.launch

object PostActionsModel : Model() {

    fun upvotePost(post: Post, onSuccess: (Post) -> Unit) = scope.launch {
        Repos.Post.upvotePost(post.id)
            .onSuccess { onSuccess(post.copy(vote = Vote.Up)) }
    }

    fun downvotePost(post: Post, onSuccess: (Post) -> Unit) = scope.launch {
        Repos.Post.upvotePost(post.id)
            .onSuccess { onSuccess(post.copy(vote = Vote.Down)) }
    }

    fun unvotePost(post: Post, onSuccess: (Post) -> Unit) = scope.launch {
        Repos.Post.upvotePost(post.id)
            .onSuccess { onSuccess(post.copy(vote = Vote.None)) }
    }

    fun hidePost(post: Post, onSuccess: (Post) -> Unit) = scope.launch {
        Repos.Post.upvotePost(post.id)
            .onSuccess { onSuccess(post.copy(isHidden = true)) }
    }

    fun unHidePost(post: Post, onSuccess: (Post) -> Unit) = scope.launch {
        Repos.Post.upvotePost(post.id)
            .onSuccess { onSuccess(post.copy(isHidden = false)) }
    }

}