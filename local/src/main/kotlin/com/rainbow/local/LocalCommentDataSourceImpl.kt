package com.rainbow.local

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalCommentDataSourceImpl : LocalCommentDataSource {

    private val mutableHomeComments = MutableStateFlow(emptyList<Comment>())
    override val homeComments get() = mutableHomeComments.asStateFlow()

    private val mutablePostComments = MutableStateFlow(emptyList<Comment>())
    override val postComments get() = mutablePostComments.asStateFlow()

    private val allComments = listOf(
        mutableHomeComments,
        mutablePostComments,
    )

    override fun insertHomeComment(comment: Comment) {
        mutableHomeComments.value = homeComments.value + comment
    }

    override fun insertPostComment(comment: Comment) {
        mutablePostComments.value = postComments.value + comment
    }

    override fun updatePostComment(comment: Comment) {
        mutablePostComments.value = postComments.value.map { oldComment ->
            if (oldComment.id == comment.id)
                comment
            else
                oldComment
        }
    }

    override fun upvoteComment(commentId: String) {
        allComments.forEach { state ->
            state.value = state.value.map { comment ->
                if (comment.id == commentId)
                    comment.copy(vote = Vote.Up)
                else
                    comment
            }
        }
    }

    override fun downvoteComment(commentId: String) {
        allComments.forEach { state ->
            state.value = state.value.map { comment ->
                if (comment.id == commentId)
                    comment.copy(vote = Vote.Down)
                else
                    comment
            }
        }
    }

    override fun unvoteComment(commentId: String) {
        allComments.forEach { state ->
            state.value = state.value.map { comment ->
                if (comment.id == commentId)
                    comment.copy(vote = Vote.None)
                else
                    comment
            }
        }
    }

    override fun clearHomeComments() {
        mutableHomeComments.value = emptyList()
    }

    override fun clearPostComments() {
        mutablePostComments.value = emptyList()
    }

    override fun clearThreadComments(parentId: String) {
        mutablePostComments.value = postComments.value.filterNot { it.isContinueThread && it.id == parentId }
    }

}