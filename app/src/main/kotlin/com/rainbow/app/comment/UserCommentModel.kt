package com.rainbow.app.comment

import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import kotlinx.coroutines.launch

private val userCommentModels = mutableSetOf<UserCommentModel>()

class UserCommentModel private constructor(private val userName: String) : CommentModel() {
    override fun loadComments() {
        scope.launch {
            mutableComments.value = Repos.Comment.getUserComments(userName).toUIState()
        }
    }

    companion object {
        fun getOrCreateInstance(userName: String): UserCommentModel {
            return userCommentModels.find { it.userName == userName }
                ?: UserCommentModel(userName).also { userCommentModels += it }
        }
    }
}