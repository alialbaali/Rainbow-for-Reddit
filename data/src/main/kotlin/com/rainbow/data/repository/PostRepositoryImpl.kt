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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    override val posts: Flow<List<Post>>
        get() = localPostDataSource.posts.flowOn(dispatcher)

    override val homePosts: Flow<List<Post>>
        get() = localPostDataSource.homePosts.flowOn(dispatcher)

    override val profileSubmittedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileSubmittedPosts.flowOn(dispatcher)

    override val profileUpvotedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileUpvotedPosts.flowOn(dispatcher)

    override val profileDownvotedPosts: Flow<List<Post>>
        get() = localPostDataSource.profileDownvotedPosts.flowOn(dispatcher)

    override val profileHiddenPosts: Flow<List<Post>>
        get() = localPostDataSource.profileHiddenPosts.flowOn(dispatcher)

    override val userSubmittedPosts: Flow<List<Post>>
        get() = localPostDataSource.userSubmittedPosts.flowOn(dispatcher)

    override val subredditPosts: Flow<List<Post>>
        get() = localPostDataSource.subredditPosts.flowOn(dispatcher)

    override val searchPosts: Flow<List<Post>>
        get() = localPostDataSource.searchPosts.flowOn(dispatcher)

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

    private suspend fun List<Post>.mapWithSubredditImageUrl() = map {
        val subredditImageUrl = remoteSubredditDataSource.getSubreddit(it.subredditName)
            .let(subredditMapper::map).imageUrl
        it.copy(subredditImageUrl = subredditImageUrl)
    }
}