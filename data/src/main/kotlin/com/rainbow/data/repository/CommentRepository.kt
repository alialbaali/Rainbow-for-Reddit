package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import com.rainbow.sql.LocalCommentQueries
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal fun CommentRepository(
    remoteDataSource: RemoteCommentDataSource,
    localDataSource: LocalCommentQueries,
    settings: FlowSettings,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemoteComment, Comment>,
): CommentRepository = CommentRepositoryImpl(remoteDataSource, localDataSource, settings, dispatcher, mapper)

@OptIn(ExperimentalSettingsApi::class)
private class CommentRepositoryImpl(
    private val remoteCommentDataSource: RemoteCommentDataSource,
    private val localCommentQueries: LocalCommentQueries,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteComment, Comment>,
) : CommentRepository {

    override fun getCurrentUserComments(): Flow<Result<List<Comment>>> = flow {
        val currentUserId = settings.getString(SettingsKeys.UserName)
        remoteCommentDataSource.getUserComments(currentUserId)
            .mapCatching { it.quickMap(mapper) }
            .also { emit(it) }
    }

    override fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Flow<Result<List<Comment>>> = flow {
        remoteCommentDataSource.getPostComments(postId, commentsSorting.name.lowercase())
            .mapCatching { it.quickMap(mapper) }
            .also { emit(it) }
    }

    override suspend fun getUserComments(userId: String): Flow<Result<List<Comment>>> = flow {
        remoteCommentDataSource.getUserComments(userId)
            .mapCatching { it.quickMap(mapper) }
            .also { emit(it) }
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