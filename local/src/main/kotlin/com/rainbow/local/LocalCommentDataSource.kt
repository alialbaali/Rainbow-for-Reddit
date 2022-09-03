package com.rainbow.local

import com.rainbow.domain.models.Comment
import kotlinx.coroutines.flow.Flow

interface LocalCommentDataSource {

    val homeComments: Flow<List<Comment>>

    val postComments: Flow<List<Comment>>

    val profileComments: Flow<List<Comment>>

    val userComments: Flow<List<Comment>>

    fun insertHomeComment(comment: Comment)

    fun insertPostComment(comment: Comment)

    fun insertProfileComment(comment: Comment)

    fun insertUserComment(comment: Comment)

    fun updatePostComment(comment: Comment)

    fun upvoteComment(commentId: String)

    fun downvoteComment(commentId: String)

    fun unvoteComment(commentId: String)

    fun clearHomeComments()

    fun clearPostComments()

    fun clearProfileComments()

    fun clearUserComments()

    fun clearThreadComments(parentId: String)

}