package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.impl.Endpoint.Posts
import com.rainbow.remote.source.RemotePostDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemotePostDataSource(client: HttpClient = mainClient): RemotePostDataSource = RemotePostDataSourceImpl(client)

private class RemotePostDataSourceImpl(private val client: HttpClient) : RemotePostDataSource {

    override suspend fun getMainPagePosts(
        mainPageSorting: String,
        timeSorting: String,
        lastPostIdPrefixed: String?,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.MainPagePosts(mainPageSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, lastPostIdPrefixed)
        }.mapCatching { it.toList() }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.SubredditPosts(subredditName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
        }.mapCatching { it.toList() }
    }

    override suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
    ): Result<List<RemotePost>> {
        return client.get<Listing<RemotePost>>(Posts.UserPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
        }.mapCatching { it.toList() }
    }

    override suspend fun upvotePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Upvote) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvotePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Unvote) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvotePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Downvote) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    override suspend fun savePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Save) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unSavePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.UnSave) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun hidePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Hide) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unHidePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.UnHide) {
            parameter(Keys.Id, postIdPrefixed)
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

    override suspend fun deletePost(postIdPrefixed: String): Result<Unit> {
        return client.submitForm(Posts.Delete) {
            parameter(Keys.Id, postIdPrefixed)
        }
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