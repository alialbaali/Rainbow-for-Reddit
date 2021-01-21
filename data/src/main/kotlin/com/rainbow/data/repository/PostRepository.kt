package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.asPostIdPrefixed
import com.rainbow.domain.models.MainPage
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Post.Type
import com.rainbow.domain.models.PostsSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun PostRepository(
    remotePostDataSource: RemotePostDataSource,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemotePost, Post>,
): PostRepository = PostRepositoryImpl(remotePostDataSource, dispatcher, mapper)

private class PostRepositoryImpl(
    private val remotePostDataSource: RemotePostDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemotePost, Post>,
) : PostRepository {

    var lastPostIdPrefixed: String? = null

    override suspend fun getMyPosts(
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getMainPagePosts(
        mainPage: MainPage,
        timeSorting: TimeSorting,
        isNext: Boolean,
    ): Result<List<Post>> = withContext(dispatcher) {

        remotePostDataSource.getMainPagePosts(
            mainPage.name.toLowerCase(),
            timeSorting.name.toLowerCase(),
            lastPostIdPrefixed.takeIf { isNext },
        )
            .mapCatching { it.quickMap(mapper) }
            .onSuccess { posts -> lastPostIdPrefixed = posts.lastOrNull()?.id?.asPostIdPrefixed() }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>> = withContext(dispatcher) {
        remotePostDataSource.getSubredditPosts(
            subredditName,
            postsSorting.name.toLowerCase(),
            timeSorting.name.toLowerCase()
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getUserPosts(
        userName: String,
        postsSorting: PostsSorting,
        timeSorting: TimeSorting,
    ): Result<List<Post>> = withContext(dispatcher) {
        remotePostDataSource.getUserPosts(
            userName,
            postsSorting.name.toLowerCase(),
            timeSorting.name.toLowerCase()
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun createPost(post: Post): Result<Post> = withContext(dispatcher) {
        with(post) {
            when (val postType = type) {
                is Type.None -> remotePostDataSource.submitTextPost(subredditName, title, null, isNSFW, isSpoiler)
                is Type.Text -> remotePostDataSource.submitTextPost(subredditName,
                    title,
                    postType.body,
                    isNSFW,
                    isSpoiler)
                is Type.Link -> remotePostDataSource.submitUrlPost(subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler)
                is Type.Gif -> remotePostDataSource.submitUrlPost(subredditName, title, postType.url, isNSFW, isSpoiler)
                is Type.Image -> remotePostDataSource.submitUrlPost(subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler)
                is Type.Video -> remotePostDataSource.submitUrlPost(subredditName,
                    title,
                    postType.url,
                    isNSFW,
                    isSpoiler)
            }
        }.map { post }
    }

    override suspend fun deletePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.deletePost(postId.asPostIdPrefixed())
    }

    override suspend fun upvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.upvotePost(postId.asPostIdPrefixed())
    }

    override suspend fun unvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.upvotePost(postId.asPostIdPrefixed())
    }

    override suspend fun downvotePost(postId: String): Result<Unit> = withContext(dispatcher) {
        remotePostDataSource.downvotePost(postId.asPostIdPrefixed())
    }

}