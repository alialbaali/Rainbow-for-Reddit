package com.rainbow.app.comment

import com.rainbow.app.model.Model
import com.rainbow.app.utils.Constants
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.map
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val postCommentListModels = mutableSetOf<PostCommentListModel>()

@OptIn(FlowPreview::class)
class PostCommentListModel private constructor(private val postId: String, private val parentId: String?) : Model() {

    private val mutableComments = MutableStateFlow<UIState<List<Comment>>>(UIState.Loading)
    val comments get() = mutableComments.asStateFlow()

    private val mutableSorting = MutableStateFlow(PostCommentSorting.Default)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    companion object {
        fun getOrCreateInstance(postId: String, parentId: String?): PostCommentListModel {
            return postCommentListModels.find { it.postId == postId && it.parentId == parentId }
                ?: PostCommentListModel(postId, parentId).also { postCommentListModels += it }
        }
    }

    init {
        loadComments()

        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach { loadComments() }
            .launchIn(scope)
    }

    fun loadPostComments() {
        mutableComments.value = UIState.Loading
        scope.launch {
            mutableComments.value = Repos.Comment.getPostsComments(postId, sorting.value)
                .onSuccess { mutableCommentsVisibility.value = it.associate { it.id to true } }
                .toUIState()
        }
    }

    fun loadMoreComments(commentId: String, children: List<String>) {
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

    fun loadContinueThreadComments(parentId: String) {
        mutableComments.value = UIState.Loading
        scope.launch {
            mutableComments.value = Repos.Comment.getContinueThreadComments(postId, parentId, sorting.value)
                .onSuccess { mutableCommentsVisibility.value = it.associate { it.id to true } }
                .toUIState()
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

    fun refreshComments() {
        mutableRefreshContent.tryEmit(Unit)
    }

    private fun List<Comment>.updateComment(comment: Comment): List<Comment> {
        return map {
            if (it.id == comment.id)
                comment.copy(replies = it.replies, type = it.type)
            else
                it.copy(replies = it.replies.updateComment(comment))
        }
    }

    private fun loadComments() = if (parentId == null) loadPostComments() else loadContinueThreadComments(parentId)
}