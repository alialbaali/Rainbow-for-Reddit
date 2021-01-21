package com.rainbow.domain.repository

import com.rainbow.domain.models.Comment

interface CommentRepository {

    suspend fun getMyComments(): Result<List<Comment>>

    suspend fun getPostsComments(postId: String): Result<List<Comment>>

    suspend fun getUserComments(userId: String): Result<List<Comment>>

    suspend fun createComment(comment: Comment): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}