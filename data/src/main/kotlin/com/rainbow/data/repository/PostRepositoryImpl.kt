package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.local.LocalItemDataSource
import com.rainbow.local.LocalPostDataSource
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class PostRepositoryImpl(
    private val subredditRepository: SubredditRepository,
    private val remotePostDataSource: RemotePostDataSource,
    private val localPostDataSource: LocalPostDataSource,
    private val localItemDataSource: LocalItemDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val postMapper: Mapper<RemotePost, Post>,
) : PostRepository {

    override val posts: Flow<List<Post>>
        get() = localPostDataSource.posts.flowOn(dispatcher)

    override val homePosts: Flow<List<Post>>
        get() = localPostDataSource.homePosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val profileSubmittedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileSubmittedPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val profileUpvotedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileUpvotedPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val profileDownvotedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileDownvotedPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val profileHiddenPosts: Flow<List<Post>>
        get() = localPostDataSource.profileHiddenPosts
            .map { it.filter { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val userSubmittedPosts: Flow<List<Post>>
        get() = localPostDataSource.userSubmittedPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val subredditPosts: Flow<List<Post>>
        get() = localPostDataSource.subredditPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override val searchPosts: Flow<List<Post>>
        get() = localPostDataSource.searchPosts
            .map { it.filterNot { post -> post.isHidden } }
            .flowOn(dispatcher)

    override suspend fun getProfileSubmittedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearProfileSubmittedPosts()
            remotePostDataSource.getUserSubmittedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertProfileSubmittedPost)
        }
    }

    override suspend fun getProfileUpvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearProfileUpvotedPosts()
            remotePostDataSource.getUserUpvotedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertProfileUpvotedPost)
        }
    }

    override suspend fun getProfileDownvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearProfileDownvotedPosts()
            remotePostDataSource.getUserDownvotedPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertProfileDownvotedPost)
        }
    }

    override suspend fun getProfileHiddenPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearProfileHiddenPosts()
            remotePostDataSource.getUserHiddenPosts(
                settings.getString(SettingsKeys.UserName),
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertProfileHiddenPost)
        }
    }

    override suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearUserSubmittedPosts()

            remotePostDataSource.getUserSubmittedPosts(
                userName,
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper).mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertUserSubmittedPost)
        }
    }

    override suspend fun getHomePosts(
        postsSorting: HomePostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearHomePosts()
            remotePostDataSource.getHomePosts(
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertHomePost)
        }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: SubredditPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearSubredditPosts()
            remotePostDataSource.getSubredditPosts(
                subredditName,
                postsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId,
            ).quickMap(postMapper)
                .mapWithSubredditImageUrl()
                .forEach(localPostDataSource::insertSubredditPost)
        }
    }

    override fun getPost(postId: String): Flow<Result<Post>> {
        return localPostDataSource.getPost(postId)
            .map { post ->
                post ?: remotePostDataSource.getPost(postId)
                    .let(postMapper::map)
                    .also(localPostDataSource::insertPost)
            }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
            .flowOn(dispatcher)
    }

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

    override suspend fun upvotePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.upvotePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(vote = Vote.Up)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(vote = Vote.Up)
            }
        }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.unvotePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(vote = Vote.None)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(vote = Vote.None)
            }
        }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.downvotePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(vote = Vote.Down)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(vote = Vote.Down)
            }
        }
    }

    override suspend fun hidePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.hidePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(isHidden = true)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(isHidden = true)
            }
        }
    }

    override suspend fun unHidePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.unHidePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(isHidden = false)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(isHidden = false)
            }
        }
    }

    override suspend fun savePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.savePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(isSaved = true)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(isSaved = true)
            }
        }
    }

    override suspend fun unSavePost(postId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remotePostDataSource.unSavePost(postId)
            localPostDataSource.updatePost(postId) { post ->
                post.copy(isSaved = false)
            }
            localItemDataSource.updateItem<Post>(postId) { post ->
                post.copy(isSaved = false)
            }
        }
    }

    override suspend fun searchPosts(
        searchTerm: String,
        postSorting: SearchPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastPostId == null) localPostDataSource.clearSearchPosts()

            remotePostDataSource.searchPosts(
                searchTerm,
                postSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastPostId
            ).quickMap(postMapper).forEach(localPostDataSource::insertSearchPost)
        }
    }

    private suspend fun List<Post>.mapWithSubredditImageUrl() = coroutineScope {
        val result = map { post ->
            post.id to async {
                subredditRepository.getSubreddit(post.subredditName)
                    .firstOrNull()?.getOrNull()?.imageUrl
            }
        }
        map { post ->
            val subredditImageUrl = result.first { it.first == post.id }.second
            post.copy(subredditImageUrl = subredditImageUrl.await())
        }
    }
}