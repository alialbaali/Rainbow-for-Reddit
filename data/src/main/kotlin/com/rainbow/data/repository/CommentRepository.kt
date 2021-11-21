package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import com.rainbow.sql.LocalComment
import com.rainbow.sql.LocalCommentQueries
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class CommentRepositoryImpl(
    private val remoteCommentDataSource: RemoteCommentDataSource,
    private val localCommentQueries: LocalCommentQueries,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemoteComment, LocalComment>,
    private val localMapper: Mapper<LocalComment, Comment>,
) : CommentRepository {

    override fun getCurrentUserComments(): Flow<Result<List<Comment>>> = flow {
        val currentUserId = settings.getString(SettingsKeys.UserName)
        localCommentQueries.clear()
        remoteCommentDataSource.getUserComments(currentUserId)
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                it.forEach {
                    if (localCommentQueries.selectById(it.id).executeAsOneOrNull() == null)
                        localCommentQueries.insert(it)
                }
            }
        localCommentQueries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }
    }

    override fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Flow<Result<List<Comment>>> = flow {
        localCommentQueries.clear()
        remoteCommentDataSource.getPostComments(postId, commentsSorting.name.lowercase())
            .map { it.quickMap(remoteMapper) }
            .onSuccess {
                it.forEach {
                    if (localCommentQueries.selectById(it.id).executeAsOneOrNull() == null)
                        localCommentQueries.insert(it)
                }
            }
        localCommentQueries.selectByParentId(postId)
            .asFlow()
            .mapToList()
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }
    }

    override suspend fun getUserComments(userId: String): Flow<Result<List<Comment>>> = flow {
        localCommentQueries.clear()
        remoteCommentDataSource.getUserComments(userId)
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                it.forEach {
                    if (localCommentQueries.selectById(it.id).executeAsOneOrNull() == null)
                        localCommentQueries.insert(it)
                }
            }
        localCommentQueries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }
    }

    override suspend fun getMoreComments(postId: String, children: List<String>, commentsSorting: PostCommentSorting) {
        remoteCommentDataSource.getMoreComments(postId, children, commentsSorting.name.lowercase())
            .map { it.quickMap(remoteMapper) }
            .onSuccess {
                it.forEach {
                    localCommentQueries.deleteById(it.id)
                    localCommentQueries.insert(it)
                }
            }
    }

    override suspend fun createComment(comment: Comment): Result<Comment> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun upvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.upvoteComment(commentId)
            .onSuccess { localCommentQueries.upvote(commentId) }
    }

    override suspend fun unvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.unvoteComment(commentId)
            .onSuccess { localCommentQueries.unvote(commentId) }
    }

    override suspend fun downvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.downvoteComment(commentId)
            .onSuccess { localCommentQueries.downvote(commentId) }
    }

}