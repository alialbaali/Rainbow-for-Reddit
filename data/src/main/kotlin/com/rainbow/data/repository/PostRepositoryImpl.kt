package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.repository.PostRepository
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import com.rainbow.sql.LocalPost
import com.rainbow.sql.RainbowDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal class PostRepositoryImpl(
    private val remoteDataSource: RemotePostDataSource,
    private val database: RainbowDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val remotePostMapper: Mapper<RemotePost, LocalPost>,
    private val localPostMapper: Mapper<LocalPost, Post>,
) : PostRepository {

    override fun getUserSubmittedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource.getUserSubmittedPosts(
            userName,
            postsSorting.name.lowercase(),
            timeSorting.name.lowercase(),
            DefaultLimit,
            lastPostId
        ).savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getUserUpvotedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource
            .getUserUpvotedPosts(
                userName,
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            )
            .savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getUserDownvotedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource
            .getUserDownvotedPosts(
                userName,
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            )
            .savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getUserHiddenPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource
            .getUserHiddenPosts(
                userName,
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            )
            .savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getUserSavedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource
            .getUserSavedPosts(
                userName,
                postsSorting.name.lowercase(),
                timeSorting.name.lowercase(),
                DefaultLimit,
                lastPostId,
            )
            .savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getHomePosts(
        postsSorting: MainPostSorting,
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
            .savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getSubredditPosts(
        subredditName: String,
        postsSorting: SubredditPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Flow<Result<List<Post>>> = flow {
        remoteDataSource.getSubredditPosts(
            subredditName,
            postsSorting.name.lowercase(),
            timeSorting.name.lowercase(),
            DefaultLimit,
            lastPostId,
        ).savePosts(this, lastPostId)
    }.flowOn(dispatcher)

    override fun getPost(postId: String): Flow<Result<Post>> = flow {
        database.localPostQueries.selectById(postId)
            .executeAsOneOrNull()
            .apply {
                if (this == null) {
                    remoteDataSource.getPost(postId)
                        .mapCatching { remotePostMapper.map(it) }
                        .onSuccess {
                            database.localPostQueries.insert(it)
                        }
                        .onFailure {
                            emit(Result.failure<Post>(it))
                        }
                }
            }

        database.localPostQueries.selectById(postId)
            .executeAsOne()
            .let { localPostMapper.map(it) }
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
                    postType.urls.first(),
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
            .onSuccess { database.localPostQueries.upvote(postId) }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unvotePost(postId)
            .onSuccess { database.localPostQueries.unvote(postId) }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.downvotePost(postId)
            .onSuccess { database.localPostQueries.downvote(postId) }
    }

    override suspend fun hidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.hidePost(postId)
            .onSuccess { database.localPostQueries.hide(postId) }
    }

    override suspend fun unHidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unHidePost(postId)
            .onSuccess { database.localPostQueries.unhide(postId) }
    }

    override suspend fun readPost(postId: String): Result<Unit> = withContext(dispatcher) {
        database.localPostQueries.read(postId).let { Result.success(it) }
    }

    override suspend fun searchPosts(searchTerm: String): Flow<Result<List<Post>>> = flow {
        remoteDataSource.searchPosts(searchTerm)
            .map { it.quickMap(remotePostMapper) }
            .map { it.quickMap(localPostMapper) }
            .also { emit(it) }
    }

    private suspend fun Result<List<RemotePost>>.savePosts(
        collector: FlowCollector<Result<List<Post>>>,
        lastPostId: String?,
    ) {
        onSuccess {
            if (lastPostId == null) {
                database.localPostQueries.clear()
                database.localAwardQueries.clear()
                database.localLinkQueries.clear()
                database.localPostFlairQueries.clear()
            }
            it.quickMap(remotePostMapper).forEach { localPost ->
                if (database.localPostQueries.selectById(localPost.id).executeAsOneOrNull() == null)
                    database.localPostQueries.insert(localPost)
            }
        }.onFailure { collector.emit(Result.failure<List<Post>>(it)) }

        database.localPostQueries.selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { it.quickMap(localPostMapper) }
            .map { Result.success(it) }
            .also { collector.emitAll(it) }
    }
}