package com.rainbow.desktop.comment

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val postCommentListModels = mutableSetOf<PostCommentListStateHolder>()

@OptIn(FlowPreview::class)
class PostCommentListStateHolder private constructor(private val type: Type) : StateHolder() {

    private val mutableComments = MutableStateFlow<UIState<List<Comment>>>(UIState.Loading(emptyList()))
    val comments get() = mutableComments.asStateFlow()

    private val initialSorting = Repos.Settings.getPostCommentSorting()
    private val mutableSorting = MutableStateFlow(initialSorting)
    val sorting get() = mutableSorting.asStateFlow()

    private val isCommentsCollapsed = Repos.Settings.getIsCommentsCollapsed()
    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    companion object {
        fun getOrCreateInstance(type: Type): PostCommentListStateHolder {
            return postCommentListModels.find { it.type == type }
                ?: PostCommentListStateHolder(type).also { postCommentListModels += it }
        }
    }

    sealed interface Type {
        val postId: String

        data class Post(override val postId: String) : Type
        data class Thread(override val postId: String, val parentId: String) : Type
    }

    init {
        loadComments()

        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach { loadComments() }
            .launchIn(scope)
    }

    private fun loadPostComments(postId: String) {
        mutableComments.value = UIState.Loading(comments.value.getOrDefault(emptyList()))
        scope.launch {
            mutableComments.value = Repos.Comment.getPostsComments(postId, sorting.value)
                .onSuccess { mutableCommentsVisibility.value = it.associate { it.id to !isCommentsCollapsed } }
                .toUIState(comments.value.getOrDefault(emptyList()))
        }
    }

    fun loadMoreComments(commentId: String, children: List<String>) {
        scope.launch {
            Repos.Comment.getMorePostComments(type.postId, children, sorting.value)
                .onSuccess { moreComments ->
                    mutableComments.value = comments.value.map { it ->
                        it.toMutableList()
                            .apply { removeIf { it.id == commentId } }
                            .plus(moreComments.filter { it.parentId == type.postId })
                            .toMutableList()
                            .replaceViewMore(commentId, moreComments)
                    }
                    mutableCommentsVisibility.value = commentsVisibility.value + when {
                        commentsVisibility.value.all { it.value } -> moreComments.associate { it.id to true }
                        commentsVisibility.value.none { it.value } -> moreComments.associate { it.id to false }
                        else -> moreComments.associate { it.id to !isCommentsCollapsed }
                    }
                }
        }
    }

    private fun loadThreadComments(postId: String, parentId: String) {
        mutableComments.value = UIState.Loading(comments.value.getOrDefault(emptyList()))
        scope.launch {
            mutableComments.value = Repos.Comment.getThreadComments(postId, parentId, sorting.value)
                .onSuccess { mutableCommentsVisibility.value = it.associate { it.id to isCommentsCollapsed } }
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
                    .replaceViewMore(commentId, comments) // Checkout this code if it's keep looping
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

    private fun loadComments() = when (type) {
        is Type.Post -> loadPostComments(type.postId)
        is Type.Thread -> loadThreadComments(type.postId, type.parentId)
    }
}
