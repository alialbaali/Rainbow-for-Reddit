package com.rainbow.domain.repository

import com.rainbow.domain.models.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCurrentUserComments(): Flow<Result<List<Comment>>>

    fun getPostsComments(postId: String): Flow<Result<List<Comment>>>

    suspend fun getUserComments(userId: String): Flow<Result<List<Comment>>>

    suspend fun createComment(comment: Comment): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}