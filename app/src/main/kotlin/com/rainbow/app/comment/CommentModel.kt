package com.rainbow.app.comment

import com.rainbow.app.utils.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.map
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class CommentModel : Model() {

    protected val mutableComments = MutableStateFlow<UIState<List<Comment>>>(UIState.Loading)
    val comments get() = mutableComments.asStateFlow()

    protected val mutableCommentSorting = MutableStateFlow(PostCommentSorting.Default)
    val commentsSorting get() = mutableCommentSorting.asStateFlow()

    protected val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    abstract fun loadComments()

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

    fun setCommentSorting(commentSorting: PostCommentSorting) {
        mutableCommentSorting.value = commentSorting
    }

    fun upvoteComment(commentId: String) {
        scope.launch {
            Repos.Comment.upvoteComment(commentId).onSuccess {
                mutableComments.value = comments.value.map {
                    it.voteComment(commentId, Vote.Up)
                }
            }
        }
    }

    fun downvoteComment(commentId: String) {
        scope.launch {
            Repos.Comment.downvoteComment(commentId).onSuccess {
                mutableComments.value = comments.value.map {
                    it.voteComment(commentId, Vote.Down)
                }
            }
        }
    }

    fun unvoteComment(commentId: String) {
        scope.launch {
            Repos.Comment.unvoteComment(commentId).onSuccess {
                mutableComments.value = comments.value.map {
                    it.voteComment(commentId, Vote.None)
                }
            }
        }
    }

    private fun List<Comment>.voteComment(commentId: String, vote: Vote): List<Comment> {
        return map { comment ->
            if (comment.id == commentId)
                comment.copy(vote = vote)
            else
                comment.copy(replies = comment.replies.voteComment(commentId, vote))
        }
    }
}