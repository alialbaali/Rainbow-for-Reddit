package com.rainbow.app.comment

import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val postCommentModels = mutableSetOf<PostCommentModel>()

class PostCommentModel private constructor(private val postId: String) : CommentModel() {

    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()

    companion object {
        fun getOrCreateInstance(postId: String): PostCommentModel {
            return postCommentModels.find { it.postId == postId }
                ?: PostCommentModel(postId).also { postCommentModels += it }
        }
    }

    init {
        loadComments()
    }

    override fun loadComments() {
        mutableComments.value = UIState.Loading
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