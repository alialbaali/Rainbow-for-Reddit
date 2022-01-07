package com.rainbow.common.post

import com.rainbow.common.comment.PostCommentListModel
import com.rainbow.common.model.Model
import com.rainbow.common.utils.UIState
import com.rainbow.common.utils.map
import com.rainbow.common.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val postScreenModels = mutableSetOf<PostScreenModel>()

class PostScreenModel private constructor(private val type: Type) : Model() {

    private val mutablePost = MutableStateFlow<UIState<Post>>(UIState.Loading)
    val post get() = mutablePost.asStateFlow()

    private val mutableCommentListModel = MutableStateFlow(PostCommentListModel.getOrCreateInstance(PostCommentListModel.Type.Post(type.postId)))
    val commentListModel get() = mutableCommentListModel.asStateFlow()

    private val mutableBackStack = MutableStateFlow(mutableListOf<PostCommentListModel>())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow(mutableListOf<PostCommentListModel>())
    val forwardStack get() = mutableForwardStack.asStateFlow()

    companion object {
        fun getOrCreateInstance(type: Type): PostScreenModel {
            return postScreenModels.find { it.type.postId == type.postId }
                ?: PostScreenModel(type).also { postScreenModels += it }
        }
    }

    init {
        loadPost()
    }

    sealed interface Type {

        val postId: String

        @JvmInline
        value class PostId(override val postId: String) : Type

        @JvmInline
        value class PostEntity(val post: Post) : Type {
            override val postId: String
                get() = post.id
        }
    }

    fun loadPost() = scope.launch {
        mutablePost.value = when (type) {
            is Type.PostEntity -> UIState.Success(type.post)
            is Type.PostId -> Repos.Post.getPost(type.postId).toUIState()
        }
    }

    fun updatePost(post: Post) {
        postScreenModels.onEach { model ->
            if (model.type.postId == post.id) {
                model.mutablePost.value = model.post.value.map {
                    if (it.id == post.id)
                        post
                    else
                        it
                }
            }
        }
    }

    fun setCommentListModel(parentId: String) {
        backStack.value += commentListModel.value
        mutableCommentListModel.value = PostCommentListModel.getOrCreateInstance(PostCommentListModel.Type.Thread(type.postId, parentId))
        forwardStack.value.clear()
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