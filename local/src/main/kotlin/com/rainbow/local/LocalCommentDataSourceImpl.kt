package com.rainbow.local

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalCommentDataSourceImpl : LocalCommentDataSource {

    private val mutableComments = MutableStateFlow(emptyList<Comment>())
    override val comments get() = mutableComments.asStateFlow()

    override fun insertComment(comment: Comment) {
        mutableComments.value = comments.value + comment
    }

    override fun upvoteComment(commentId: String) {
        mutableComments.value = comments.value.map { comment ->
            if (comment.id == commentId)
                comment.copy(vote = Vote.Up)
            else
                comment
        }
    }

    override fun downvoteComment(commentId: String) {
        mutableComments.value = comments.value.map { comment ->
            if (comment.id == commentId)
                comment.copy(vote = Vote.Down)
            else
                comment
        }
    }

    override fun unvoteComment(commentId: String) {
        mutableComments.value = comments.value.map { comment ->
            if (comment.id == commentId)
                comment.copy(vote = Vote.None)
            else
                comment
        }
    }

    override fun clearComments() {
        mutableComments.value = emptyList()
    }

}