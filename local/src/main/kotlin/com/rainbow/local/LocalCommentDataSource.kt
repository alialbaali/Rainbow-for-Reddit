package com.rainbow.local

import com.rainbow.domain.models.Comment
import kotlinx.coroutines.flow.Flow

interface LocalCommentDataSource {

    val comments: Flow<List<Comment>>

    fun insertComment(comment: Comment)

    fun upvoteComment(commentId: String)

    fun downvoteComment(commentId: String)

    fun unvoteComment(commentId: String)

    fun clearComments()

}