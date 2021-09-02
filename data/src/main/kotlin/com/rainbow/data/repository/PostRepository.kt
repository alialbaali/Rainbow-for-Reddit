package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.models.PostListSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import com.rainbow.sql.LocalPost
import com.rainbow.sql.LocalPostQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal fun PostRepository(
    remoteDataSource: RemotePostDataSource,
    localPostQueries: LocalPostQueries,
    dispatcher: CoroutineDispatcher,
    remoteMapper: Mapper<RemotePost, LocalPost>,
    localMapper: Mapper<LocalPost, Post>,
): PostRepository = PostRepositoryImpl(remoteDataSource, localPostQueries, dispatcher, remoteMapper, localMapper)

private class PostRepositoryImpl(
    private val remoteDataSource: RemotePostDataSource,
    private val queries: LocalPostQueries,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemotePost, LocalPost>,
    private val localMapper: Mapper<LocalPost, Post>,
) : PostRepository {

    override fun getMyPosts(
        postsSorting: PostListSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>> = flow {
        withContext(dispatcher) {
        }

        TODO("Not yet implemented")
    }

    override fun getHomePosts(
        postsSorting: PostListSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource
            .getHomePosts(
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            )
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                queries.transaction {
                    queries.clear()
                    it.forEach { localPost -> queries.insert(localPost) }
                }
            }.onFailure { emit(Result.failure<List<Post>>(it)) }

        queries.selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }
    }
        .flowOn(dispatcher)

    override fun getSubredditPosts(
        subredditName: String,
        postsSorting: PostListSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>> = flow {
        withContext(dispatcher) {
            remoteDataSource.getSubredditPosts(
                subredditName,
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            ).mapCatching { it.quickMap(remoteMapper) }
                .onSuccess {
                    queries.transaction {
                        queries.clear()
                        it.forEach { localPost ->
                            queries.insert(localPost)
                        }
                    }
                    queries.selectAll()
                        .asFlow()
                        .mapToList(dispatcher)
                        .map { it.quickMap(localMapper) }
                        .map { Result.success(it) }
                        .also { emitAll(it) }
                }.onFailure {
                    emit(Result.failure<List<Post>>(it))
                }
        }
    }

    override fun getUserPosts(
        userName: String,
        postListSorting: PostListSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>> = flow {
        withContext(dispatcher) {
            remoteDataSource.getUserPosts(
                userName,
                postListSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            ).mapCatching { it.quickMap(remoteMapper) }
                .onSuccess {
                    queries.transaction {
                        queries.clear()
                        it.forEach { localPost ->
                            queries.insert(localPost)
                        }
                    }
                    queries.selectAll()
                        .asFlow()
                        .mapToList(dispatcher)
                        .map { it.quickMap(localMapper) }
                        .map { Result.success(it) }
                        .also { emitAll(it) }
                }.onFailure {
                    emit(Result.failure<List<Post>>(it))
                }
        }
    }

    override fun getPost(postId: String): Flow<Result<Post>> = flow {
        queries.selectById(postId)
            .executeAsOneOrNull()
            .apply {
                if (this == null) {
                    remoteDataSource.getPost(postId)
                        .mapCatching { remoteMapper.map(it) }
                        .onSuccess {
                            queries.insert(it)
                        }
                        .onFailure {
                            emit(Result.failure<Post>(it))
                        }
                }
            }

        queries.selectById(postId)
            .executeAsOne()
            .let { localMapper.map(it) }
            .also { emit(Result.success(it)) }
    }.flowOn(dispatcher)

    override suspend fun createPost(post: Post): Result<Unit> = withContext(dispatcher) {
        with(post) {
            when (val postType = type) {
                is Type.None -> remoteDataSource.submitTextPost(subredditName, title, null, isNSFW, isSpoiler)
                is Type.Text -> remoteDataSource.submitTextPost(
                    subredditName,
                    title,
                    postType.body,
                    isNSFW,
                    isSpoiler
                )
                is Type.Link -> remoteDataSource.submitUrlPost(
                    subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler
                )
                is Type.Gif -> remoteDataSource.submitUrlPost(subredditName, title, postType.url, isNSFW, isSpoiler)
                is Type.Image -> remoteDataSource.submitUrlPost(
                    subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler
                )
                is Type.Video -> remoteDataSource.submitUrlPost(
                    subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler
                )
            }
        }
    }

    override suspend fun deletePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.deletePost(postId)
    }

    override suspend fun upvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.upvotePost(postId)
            .onSuccess { queries.upvote(postId) }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unvotePost(postId)
            .onSuccess { queries.unvote(postId) }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.downvotePost(postId)
            .onSuccess { queries.downvote(postId) }
    }

}