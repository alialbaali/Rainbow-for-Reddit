package com.rainbow.desktop.post

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PostCommentsModel(val parentId: String? = null, val comments: List<Comment> = emptyList())

@OptIn(FlowPreview::class)
class PostScreenStateHolder(
    private val postId: String,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
) : StateHolder() {

    val post = postRepository.getPost(postId)
        .map { it.toUIState() }
        .stateIn(scope, SharingStarted.Eagerly, UIState.Empty)

    private val mutableCommentSorting = MutableStateFlow(PostCommentSorting.Default)
    val commentSorting get() = mutableCommentSorting.asStateFlow()

    private val mutableComments = MutableStateFlow<UIState<PostCommentsModel>>(UIState.Loading())
    val comments get() = mutableComments.asStateFlow()

    private val mutableCommentsVisibility = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentsVisibility get() = mutableCommentsVisibility.asStateFlow()

    private val mutableBackStack = MutableStateFlow(emptyList<PostCommentsModel>())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow(emptyList<PostCommentsModel>())
    val forwardStack get() = mutableForwardStack.asStateFlow()

    private val mutableThreadParentId = MutableStateFlow<String?>(null)

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    companion object {
        private val stateHolders = mutableListOf<PostScreenStateHolder>()

        fun getOrCreateInstance(postId: String): PostScreenStateHolder {
            return stateHolders.find { it.postId == postId }
                ?: PostScreenStateHolder(postId).also(stateHolders::add)
        }
    }

    init {
        loadPostComments()

        combine(
            mutableThreadParentId,
            commentRepository.postComments,
        ) { parentId, comments ->
            val postComments = comments.filterNot { it.isContinueThread }
            val threadComments = comments.filter { it.isContinueThread && it.id == parentId }

            if (parentId == null) {
                mutableComments.value = UIState.Success(PostCommentsModel(null, postComments))
            } else {
                val isNotInBackStack = backStack.value.none { it.parentId == parentId || it.parentId == null }
                val currentComments = this.comments.value.getOrNull()
                if (isNotInBackStack && currentComments != null) {
                    mutableBackStack.value = backStack.value + currentComments
                }
                mutableComments.value = UIState.Success(PostCommentsModel(parentId, threadComments))
                mutableForwardStack.value = emptyList()
            }

            mutableCommentsVisibility.value = comments.flattenRecursively()
                .associate { comment -> comment.id to true }
        }.launchIn(scope)

        mutableThreadParentId
            .filterNotNull()
            .onEach { parentId ->
                val isThreadNotLoaded = comments.value.getOrDefault(PostCommentsModel()).comments
                    .none { it.isContinueThread && it.id == parentId }
                if (isThreadNotLoaded) loadThreadComments(parentId)
            }
            .launchIn(scope)

        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach {
                val parentId = mutableThreadParentId.value
                if (parentId == null) {
                    loadPostComments()
                } else {
                    loadThreadComments(parentId)
                }
            }
            .launchIn(scope)
    }

    private fun loadPostComments() = scope.launch {
        mutableComments.value = UIState.Loading(comments.value.getOrNull())
        commentRepository.getPostsComments(
            postId,
            commentSorting.value,
        ).onFailure {
            mutableComments.value = UIState.Failure(comments.value.getOrNull(), it)
        }
    }

    fun loadViewMoreComments(commentId: String, children: List<String>) = scope.launch {
        mutableComments.value = UIState.Loading(comments.value.getOrNull())
        commentRepository.getViewMoreComments(
            postId,
            commentId,
            children,
            commentSorting.value,
        ).onFailure {
            mutableComments.value = UIState.Failure(comments.value.getOrNull(), it)
        }
    }

    private fun loadThreadComments(parentId: String) = scope.launch {
        mutableComments.value = UIState.Loading()
        commentRepository.getThreadComments(
            postId,
            parentId,
            commentSorting.value,
        ).onFailure {
            mutableComments.value = UIState.Failure(comments.value.getOrNull(), it)
        }
    }

    fun collapseComments() {
        mutableCommentsVisibility.value = commentsVisibility.value.mapValues { false }
    }

    fun expandComments() {
        mutableCommentsVisibility.value = commentsVisibility.value.mapValues { true }
    }

    fun setCommentVisibility(commentId: String, isVisible: Boolean) {
        mutableCommentsVisibility.value = commentsVisibility.value
            .mapValues {
                if (it.key == commentId)
                    isVisible
                else
                    it.value
            }
    }

    fun refreshComments() {
        mutableRefreshContent.tryEmit(Unit)
    }

    fun setCommentSorting(commentSorting: PostCommentSorting) {
        mutableCommentSorting.value = commentSorting
        loadPostComments()
    }

    fun back() {
        mutableForwardStack.value = forwardStack.value + comments.value.getOrDefault(PostCommentsModel())
        mutableThreadParentId.value = backStack.value.last().parentId
        mutableBackStack.value = backStack.value.dropLast(1)
    }

    fun forward() {
        mutableBackStack.value = backStack.value + comments.value.getOrDefault(PostCommentsModel())
        mutableThreadParentId.value = forwardStack.value.last().parentId
        mutableForwardStack.value = forwardStack.value.dropLast(1)
    }

    fun setThreadParentId(parentId: String) {
        mutableThreadParentId.value = parentId
    }

    private fun List<Comment>.flattenRecursively(): List<Comment> {
        val result = mutableListOf<Comment>()
        for (element in this) {
            result.add(element)
            result.addAll(element.replies.flattenRecursively())
        }
        return result
    }
}