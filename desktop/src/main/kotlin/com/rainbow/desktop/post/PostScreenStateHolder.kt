package com.rainbow.desktop.post

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.PostCommentListStateHolder
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val postScreenModels = mutableSetOf<PostScreenStateHolder>()

class PostScreenStateHolder(private val postId: String) : StateHolder() {

    private val mutablePost = MutableStateFlow<UIState<Post>>(UIState.Empty)
    val post get() = mutablePost.asStateFlow()

    private val mutableCommentListModel =
        MutableStateFlow(PostCommentListStateHolder.getOrCreateInstance(PostCommentListStateHolder.Type.Post(postId)))
    val commentListModel get() = mutableCommentListModel.asStateFlow()

    private val mutableBackStack = MutableStateFlow(mutableListOf<PostCommentListStateHolder>())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow(mutableListOf<PostCommentListStateHolder>())
    val forwardStack get() = mutableForwardStack.asStateFlow()

    companion object {
        fun getOrCreateInstance(postId: String): PostScreenStateHolder {
            return postScreenModels.find { it.postId == postId }
                ?: PostScreenStateHolder(postId).also { postScreenModels += it }
        }
    }

    init {
        loadPost()
    }

    fun loadPost() {
        Repos.Post.getPost(postId)
            .onEach { mutablePost.value = it.toUIState() }
            .launchIn(scope)
    }

    fun setCommentListModel(parentId: String) {
//        backStack.value += commentListModel.value
//        mutableCommentListModel.value = PostCommentListStateHolder.getOrCreateInstance(
//                PostCommentListStateHolder.Type.Thread(
//                    type.postId,
//                    parentId
//                )
//            )
//        forwardStack.value.clear()
    }

    fun back() {
        forwardStack.value += commentListModel.value
        mutableCommentListModel.value = backStack.value.last()
        backStack.value.removeLast()
    }

    fun forward() {
        backStack.value += commentListModel.value
        mutableCommentListModel.value = forwardStack.value.last()
        forwardStack.value.removeLast()
    }
}