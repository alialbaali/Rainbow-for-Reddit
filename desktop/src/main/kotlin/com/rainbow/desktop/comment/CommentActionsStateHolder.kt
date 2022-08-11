package com.rainbow.desktop.comment

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.launch

object CommentActionsStateHolder : StateHolder() {

    fun upvoteComment(comment: Comment) = scope.launch {
        Repos.Comment.upvoteComment(comment.id)
    }

    fun downvoteComment(comment: Comment) = scope.launch {
        Repos.Comment.downvoteComment(comment.id)
    }

    fun unvoteComment(comment: Comment) = scope.launch {
        Repos.Comment.unvoteComment(comment.id)
    }

}