package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.*
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.repository.PostRepository
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class PostRepositoryImpl(
    private val remoteDataSource: RemotePostDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemotePost, Post>,
) : PostRepository {

    override suspend fun getCurrentUserSubmittedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getUserSubmittedPosts(
            settings.getString(SettingsKeys.UserName),
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getCurrentUserUpvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getUserUpvotedPosts(
            settings.getString(SettingsKeys.UserName),
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getCurrentUserDownvotedPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getUserDownvotedPosts(
            settings.getString(SettingsKeys.UserName),
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getCurrentUserHiddenPosts(
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getUserHiddenPosts(
            settings.getString(SettingsKeys.UserName),
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getUserSubmittedPosts(
            userName,
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getHomePosts(
        postsSorting: HomePostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getHomePosts(
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: SubredditPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.getSubredditPosts(
            subredditName,
            postsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPost(postId: String): Result<Post> = withContext(dispatcher) {
        remoteDataSource.getPost(postId)
            .mapCatching { mapper.map(it) }
    }

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
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unvotePost(postId)
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.downvotePost(postId)
    }

    override suspend fun hidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.hidePost(postId)
    }

    override suspend fun unHidePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unHidePost(postId)
    }

    override suspend fun searchPosts(
        searchTerm: String,
        postSorting: SearchPostSorting,
        timeSorting: TimeSorting,
        lastPostId: String?,
    ): Result<List<Post>> = withContext(dispatcher) {
        remoteDataSource.searchPosts(
            searchTerm,
            postSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastPostId
        ).mapCatching { it.quickMap(mapper) }
    }
}