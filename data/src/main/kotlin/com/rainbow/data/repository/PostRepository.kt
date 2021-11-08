package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.removeParameters
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.repository.PostRepository
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import com.rainbow.sql.LocalLinkPost
import com.rainbow.sql.LocalLinkPostQueries
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
    localLinkPostQueries: LocalLinkPostQueries,
    dispatcher: CoroutineDispatcher,
    remoteMapper: Mapper<RemotePost, LocalPost>,
    localMapper: Mapper<LocalPost, Post>,
): PostRepository =
    PostRepositoryImpl(remoteDataSource, localPostQueries, localLinkPostQueries, dispatcher, remoteMapper, localMapper)

private class PostRepositoryImpl(
    private val remoteDataSource: RemotePostDataSource,
    private val localPostQueries: LocalPostQueries,
    private val localLinkPostQueries: LocalLinkPostQueries,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemotePost, LocalPost>,
    private val localMapper: Mapper<LocalPost, Post>,
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
        ).savePostsAndLinks(this)
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
            .savePostsAndLinks(this)
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
            .savePostsAndLinks(this)
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
            .savePostsAndLinks(this)
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
            .savePostsAndLinks(this)
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
            .savePostsAndLinks(this)
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
        ).savePostsAndLinks(this)
    }.flowOn(dispatcher)

    override fun getPost(postId: String): Flow<Result<Post>> = flow {
        localPostQueries.selectById(postId)
            .executeAsOneOrNull()
            .apply {
                if (this == null) {
                    remoteDataSource.getPost(postId)
                        .mapCatching { remoteMapper.map(it) }
                        .onSuccess {
                            localPostQueries.insert(it)
                        }
                        .onFailure {
                            emit(Result.failure<Post>(it))
                        }
                }
            }

        localPostQueries.selectById(postId)
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
            .onSuccess { localPostQueries.upvote(postId) }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unvotePost(postId)
            .onSuccess { localPostQueries.unvote(postId) }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.downvotePost(postId)
            .onSuccess { localPostQueries.downvote(postId) }
    }

    private suspend fun Result<List<RemotePost>>.savePostsAndLinks(collector: FlowCollector<Result<List<Post>>>) {
        onSuccess {
            it.forEach {
                val id = it.name
                val url = it.url
                if (id != null && localLinkPostQueries.selectById(id).executeAsOneOrNull() == null)
                    if (!url.isNullOrBlank() && (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")))
                        localLinkPostQueries.insert(LocalLinkPost(id, url))
                    else if (!it.media?.oembed?.thumbnailUrl.isNullOrBlank())
                        localLinkPostQueries.insert(LocalLinkPost(id, it.media?.oembed?.thumbnailUrl!!))
                    else if (!it.media?.oembed?.url.isNullOrBlank())
                        localLinkPostQueries.insert(LocalLinkPost(id, it.media?.oembed?.url!!))
            }
        }
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                it.forEach { localPost ->
                    if (localPostQueries.selectById(localPost.id).executeAsOneOrNull() == null)
                        localPostQueries.insert(localPost)
                }
            }.onFailure { collector.emit(Result.failure<List<Post>>(it)) }

        localPostQueries.selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map {
                it.map {
                    val post = localMapper.map(it)
                    localLinkPostQueries.selectById(it.id)
                        .executeAsOneOrNull()
                        ?.url
                        ?.removeParameters()
                        ?.let { link ->
                            val type = when {
                                link.endsWith("jpg") || link.endsWith("png") -> Type.Image(link)
                                link.endsWith("gif") -> Type.Gif(link)
                                link.endsWith("mp4") -> Type.Video(link)
                                else -> Type.Link(link)
                            }
                            post.copy(type = type)
                        } ?: post
                }
            }
            .map { Result.success(it) }
            .also { collector.emitAll(it) }
    }
}