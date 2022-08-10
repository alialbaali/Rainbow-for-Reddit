package com.rainbow.desktop.comment

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.launch

object CommentActionsStateHolder : StateHolder() {

    fun upvoteComment(comment: Comment, onSuccess: (Comment) -> Unit) = scope.launch {
        Repos.Comment.upvoteComment(comment.id)
            .onSuccess { onSuccess(comment.copy(vote = Vote.Up)) }
    }

    fun downvoteComment(comment: Comment, onSuccess: (Comment) -> Unit) = scope.launch {
        Repos.Comment.downvoteComment(comment.id)
            .onSuccess { onSuccess(comment.copy(vote = Vote.Down)) }
    }

    fun unvoteComment(comment: Comment, onSuccess: (Comment) -> Unit) = scope.launch {
        Repos.Comment.unvoteComment(comment.id)
            .onSuccess { onSuccess(comment.copy(vote = Vote.None)) }
    }

}