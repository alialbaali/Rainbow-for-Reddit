package com.rainbow.app.post

import com.rainbow.app.comment.PostCommentListModel
import com.rainbow.app.model.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.map
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val postScreenModels = mutableSetOf<PostScreenModel>()

class PostScreenModel private constructor(private val type: Type) : Model() {

    val commentListModel = PostCommentListModel.getOrCreateInstance(type.postId)

    private val mutablePost = MutableStateFlow<UIState<Post>>(UIState.Loading)
    val post get() = mutablePost.asStateFlow()

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
}