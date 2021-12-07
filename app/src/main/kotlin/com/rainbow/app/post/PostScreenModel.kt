package com.rainbow.app.post

import com.rainbow.app.comment.PostCommentModel
import com.rainbow.app.utils.Model

private val postScreenModels = mutableSetOf<PostScreenModel>()

class PostScreenModel private constructor(private val postId: String) : Model() {

    val commentModel = PostCommentModel.getOrCreateInstance(postId)

    companion object {
        fun getOrCreateInstance(postId: String): PostScreenModel {
            return postScreenModels.find { it.postId == postId }
                ?: PostScreenModel(postId).also { postScreenModels += it }
        }
    }
}