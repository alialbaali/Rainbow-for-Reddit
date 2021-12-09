package com.rainbow.remote.impl

import com.rainbow.remote.Listing
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.get
import com.rainbow.remote.impl.Endpoint.Posts
import com.rainbow.remote.source.RemotePostDataSource
import com.rainbow.remote.submitForm
import com.rainbow.remote.toList
import io.ktor.client.*
import io.ktor.client.request.*

fun RemotePostDataSource(client: HttpClient = redditClient): RemotePostDataSource = RemotePostDataSourceImpl(client)

private class RemotePostDataSourceImpl(private val client: HttpClient) : RemotePostDataSource {

    override suspend fun getHomePosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.MainPagePosts(postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.SubredditPosts(subredditName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.UserSubmittedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getUserUpvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.UserUpvotedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getUserDownvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.UserDownvotedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getUserHiddenPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.UserHiddenPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.toList() }
    }

    override suspend fun getPost(postId: String): Result<RemotePost> {
        return client.get<Listing<RemotePost>>(Posts.GetPost) {
            parameter(Keys.Id, postId)
        }.mapCatching { it.toList().single() }
    }

    override suspend fun followPost(postId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun unFollowPost(postId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun upvotePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Upvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvotePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Unvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvotePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Downvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    override suspend fun savePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Save) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun unSavePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.UnSave) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun hidePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Hide) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun unHidePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.UnHide) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun submitTextPost(
        subredditName: String,
        title: String,
        text: String?,
        isNsfw: Boolean,
        isSpoiler: Boolean,
        resubmit: Boolean,
    ): Result<Unit> {
        return client.submitPost(subredditName, title, isNsfw, isSpoiler, resubmit) {
            parameter(Keys.Kind, Values.Self)
            parameter(Keys.Text, text)
        }
    }


    override suspend fun submitUrlPost(
        subredditName: String,
        title: String,
        url: String,
        isNsfw: Boolean,
        isSpoiler: Boolean,
        resubmit: Boolean,
    ): Result<Unit> {
        return client.submitPost(subredditName, title, isNsfw, isSpoiler, resubmit) {
            parameter(Keys.Kind, Values.Url)
            parameter(Keys.Url, url)
        }
    }

    override suspend fun deletePost(postId: String): Result<Unit> {
        return client.submitForm(Posts.Delete) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun searchPosts(searchTerm: String): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.Search) {
            parameter(Keys.Query, searchTerm)
        }.mapCatching { it.toList() }
    }

    private suspend fun HttpClient.submitPost(
        subredditName: String,
        title: String,
        isNsfw: Boolean,
        isSpoiler: Boolean,
        resubmit: Boolean,
        builder: HttpRequestBuilder.() -> Unit,
    ): Result<Unit> {
        return submitForm<Map<String, Any?>>(Posts.Submit) {
            parameter(Keys.Subreddit, subredditName)
            parameter(Keys.Title, title)
            parameter(Keys.Nsfw, isNsfw)
            parameter(Keys.Spoiler, isSpoiler)
            parameter(Keys.ReSubmit, resubmit)
            builder()
        }.mapCatching { it.getOrElse("success") { null } as Boolean? }
    }
}