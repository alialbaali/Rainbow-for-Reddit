package com.rainbow.app.comment

import com.rainbow.app.model.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.map
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val postCommentListModels = mutableSetOf<PostCommentListModel>()

class PostCommentListModel private constructor(private val postId: String) : Model() {

    private val mutableComments = MutableStateFlow<UIState<List<Comment>>>(UIState.Loading)
    val comments get() = mutableComments.asStateFlow()

    private val mutableSorting = MutableStateFlow(PostCommentSorting.Default)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()

    companion object {
        fun getOrCreateInstance(postId: String): PostCommentListModel {
            return postCommentListModels.find { it.postId == postId }
                ?: PostCommentListModel(postId).also { postCommentListModels += it }
        }
    }

    init {
        loadComments()
    }

    fun loadComments() {
        mutableComments.value = UIState.Loading
        scope.launch {
            mutableComments.value = Repos.Comment.getPostsComments(postId, sorting.value)
                .onSuccess { mutableCommentsVisibility.value = it.associate { it.id to true } }
                .toUIState()
        }
    }

    fun loadMoreComments(postId: String, commentId: String, children: List<String>) {
        scope.launch {
            Repos.Comment.getMorePostComments(postId, children, sorting.value)
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

    fun updateComment(comment: Comment) {
        postCommentListModels.onEach {
            it.mutableComments.value = it.comments.value.map { it.updateComment(comment) }
        }
    }

    fun setSorting(sorting: PostCommentSorting) {
        mutableSorting.value = sorting
        loadComments()
    }

    private fun List<Comment>.updateComment(comment: Comment): List<Comment> {
        return map {
            if (it.id == comment.id)
                comment
            else
                it.copy(replies = it.replies.updateComment(comment))
        }
    }
}