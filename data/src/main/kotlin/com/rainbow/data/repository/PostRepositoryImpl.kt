package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.repository.PostRepository
import com.rainbow.local.LocalPostDataSource
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.source.RemotePostDataSource
import com.rainbow.remote.source.RemoteSubredditDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class PostRepositoryImpl(
    private val remotePostDataSource: RemotePostDataSource,
    private val remoteSubredditDataSource: RemoteSubredditDataSource,
    private val localPostDataSource: LocalPostDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val postMapper: Mapper<RemotePost, Post>,
    private val subredditMapper: Mapper<RemoteSubreddit, Subreddit>,
) : PostRepository {

    override val posts: Flow<List<Post>> get() = localPostDataSource.posts

    override suspend fun getCurrentUserSubmittedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.getUserSubmittedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
        }
    }

    override suspend fun getCurrentUserUpvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.getUserUpvotedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
        }
    }

    override suspend fun getCurrentUserDownvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.getUserDownvotedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
        }
    }

    override suspend fun getCurrentUserHiddenPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.getUserHiddenPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
        }
    }

    override suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearPosts()

            remotePostDataSource.getUserSubmittedPosts(
                userName,
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper).mapWithSubredditImageUrl().forEach { post ->
                localPostDataSource.insertPost(post)
            }
        }
    }

    override suspend fun getHomePosts(
        postsSorting: HomePostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearPosts()
            remotePostDataSource.getHomePosts(
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper).mapWithSubredditImageUrl().forEach { post ->
                localPostDataSource.insertPost(post)
            }
        }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: SubredditPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearPosts()
            remotePostDataSource.getSubredditPosts(
                subredditName,
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper).mapWithSubredditImageUrl().forEach { post ->
                localPostDataSource.insertPost(post)
            }
        }
    }

    override fun getPost(postId: String): Flow<Result<Post>> = flow {
        posts.collect {
            if (it.isNotEmpty()) {
                val cachedPost = it.firstOrNull { post -> post.id == postId }
                val result = if (cachedPost != null) {
                    Result.success(cachedPost)
                } else {
                    remotePostDataSource.getPost(postId)
                        .let(postMapper::map)
                        .also(localPostDataSource::insertPost)
                        .let { Result.success(it) }
                }
                emit(result)
            }
        }
    }.catch { emit(Result.failure(it)) }.flowOn(dispatcher)

    override suspend fun createPost(post: Post): Result<Unit> = withContext(dispatcher) {
        with(post) {
            when (val postType = type) {
                is Type.None -> remotePostDataSource.submitTextPost(subredditName, title, null, isNSFW, isSpoiler)
                is Type.Text -> remotePostDataSource.submitTextPost(
                    subredditName,
                    title,
                    postType.body,
                    isNSFW,
                    isSpoiler
                )

                is Type.Link -> remotePostDataSource.submitUrlPost(
                    subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler
                )

                is Type.Gif -> remotePostDataSource.submitUrlPost(subredditName, title, postType.url, isNSFW, isSpoiler)
                is Type.Image -> remotePostDataSource.submitUrlPost(
                    subredditName,
                    title,
                    postType.urls.first(),
                    isNSFW,
                    isSpoiler
                )

                is Type.Video -> remotePostDataSource.submitUrlPost(
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
        remotePostDataSource.deletePost(postId)
    }

    override suspend fun upvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.upvotePost(postId)
            .onSuccess { localPostDataSource.upvotePost(postId) }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.unvotePost(postId)
            .onSuccess { localPostDataSource.unvotePost(postId) }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.downvotePost(postId)
            .onSuccess { localPostDataSource.downvotePost(postId) }
    }

    override suspend fun hidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.hidePost(postId)
    }

    override suspend fun unHidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.unHidePost(postId)
    }

    override suspend fun searchPosts(
        searchTerm: String,
        postSorting: SearchPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.searchPosts(
                searchTerm,
                postSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
        }
    }

    private suspend fun List<Post>.mapWithSubredditImageUrl() = map {
        val subredditImageUrl = remoteSubredditDataSource.getSubreddit(it.subredditName)
            .getOrNull()
            ?.let(subredditMapper::map)
            ?.imageUrl
        it.copy(subredditImageUrl = subredditImageUrl)
    }
}