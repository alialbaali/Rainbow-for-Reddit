package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.domain.models.Comment
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun CommentRepository(
    remoteDataSource: RemoteCommentDataSource,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemoteComment, Comment>,
): CommentRepository = CommentRepositoryImpl(remoteDataSource, dispatcher, mapper)

private class CommentRepositoryImpl(
    private val remoteCommentDataSource: RemoteCommentDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteComment, Comment>,
) : CommentRepository {

    override suspend fun getMyComments(): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getUserComments("userId")
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPostsComments(postId: String): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getPostComments(postId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getUserComments(userId: String): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getUserComments(userId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun createComment(comment: Comment): Result<Comment> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun upvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.upvoteComment(commentId)
    }

    override suspend fun unvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.unvoteComment(commentId)
    }

    override suspend fun downvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.downvoteComment(commentId)
    }

}