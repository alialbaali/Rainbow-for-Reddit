package com.rainbow.app.comment

import com.rainbow.app.utils.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.asSuccess
import com.rainbow.app.utils.map
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object PostCommentModel : Model() {

    private val mutableComments = MutableStateFlow<UIState<List<Comment>>>(UIState.Loading)
    val comments get() = mutableComments.asStateFlow()

    private val mutableCommentSorting = MutableStateFlow(PostCommentSorting.Default)
    val commentsSorting get() = mutableCommentSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()


    fun getComments(postId: String) {
        scope.launch {
            mutableComments.value = Repos.Comment.getPostsComments(postId, commentsSorting.value).toUIState()
            if (comments.value.isSuccess)
                mutableCommentsVisibility.value = comments.value.asSuccess().value.associate { it.id to true }
        }
    }

    fun getMoreComments(postId: String, commentId: String, children: List<String>) {
        scope.launch {
            Repos.Comment.getMoreComments(postId, children, commentsSorting.value)
                .onSuccess { moreComments ->
                    mutableComments.value = comments.value.map { it ->
                        it.toMutableList()
                            .apply { removeIf { it.id == commentId } }
                            .plus(moreComments.filter { it.parentId == postId })
                            .toMutableList()
                            .replaceViewMore(commentId, moreComments)
                    }
                    if (commentsVisibility.value.none { it.value })
                        mutableCommentsVisibility.value =
                            commentsVisibility.value + moreComments.associate { it.id to false }
                }
        }
    }

    private fun MutableList<Comment>.replaceViewMore(
        commentId: String,
        comments: List<Comment>,
    ): List<Comment> {
        return map { comment ->
            val newReplies = comment.replies.toMutableList()
                .also { it.removeIf { it.id == commentId } } + comments.filter { it.parentId == comment.id }
            comment.copy(
                replies = newReplies.toMutableList()
                    .replaceViewMore(commentId, comments)
            )
        }
    }

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

    fun collapseComments() {
        mutableCommentsVisibility.value = commentsVisibility.value.mapValues { false }
    }

    fun expandComments() {
        mutableCommentsVisibility.value = commentsVisibility.value.mapValues { true }
    }

    fun setCommentVisibility(commentId: String, isVisible: Boolean) {
        mutableCommentsVisibility.value = commentsVisibility.value.toMutableMap()
            .apply { this[commentId] = isVisible }
    }
}