package com.rainbow.local

import com.rainbow.domain.models.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalCommentDataSourceImpl : LocalCommentDataSource {

    private val mutableHomeComments = MutableStateFlow(emptyList<Comment>())
    override val homeComments get() = mutableHomeComments.asStateFlow()

    private val mutablePostComments = MutableStateFlow(emptyList<Comment>())
    override val postComments get() = mutablePostComments.asStateFlow()

    private val mutableProfileComments = MutableStateFlow(emptyList<Comment>())
    override val profileComments = mutableProfileComments.asStateFlow()

    private val mutableUserComments = MutableStateFlow(emptyList<Comment>())
    override val userComments get() = mutableUserComments.asStateFlow()

    private val allComments = listOf(
        mutableHomeComments,
        mutablePostComments,
        mutableProfileComments,
        mutableUserComments,
    )

    override fun insertHomeComment(comment: Comment) {
        mutableHomeComments.value = homeComments.value + comment
    }

    override fun insertPostComment(comment: Comment) {
        mutablePostComments.value = postComments.value + comment
    }

    override fun insertProfileComment(comment: Comment) {
        mutableProfileComments.value = profileComments.value + comment
    }

    override fun insertUserComment(comment: Comment) {
        mutableUserComments.value = userComments.value + comment
    }

    override fun updateComment(commentId: String, block: (Comment) -> Comment) {
        allComments.forEach { state ->
            state.value = state.value.map { comment ->
                if (comment.id == commentId)
                    block(comment)
                else
                    comment
            }
        }
    }

    override fun clearHomeComments() {
        mutableHomeComments.value = emptyList()
    }

    override fun clearPostComments() {
        mutablePostComments.value = emptyList()
    }

    override fun clearProfileComments() {
        mutableProfileComments.value = emptyList()
    }

    override fun clearUserComments() {
        mutableUserComments.value = emptyList()
    }

    override fun clearThreadComments(parentId: String) {
        mutablePostComments.value = postComments.value.filterNot { it.isContinueThread && it.id == parentId }
    }

}