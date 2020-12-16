package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.impl.Endpoint.*
import com.rainbow.remote.source.RemotePostDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemotePostDataSource(client: HttpClient = mainClient): RemotePostDataSource = RemotePostDataSourceImpl(client)

private class RemotePostDataSourceImpl(private val client: HttpClient) : RemotePostDataSource {

    override suspend fun getMainPagePosts(
        mainPageSorting: String,
        timeSorting: String,
        lastPostIdPrefixed: String?,
    ): RedditResponse<Listing<RemotePost>> {
        return client.redditGet(mainPageSorting) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, lastPostIdPrefixed)
        }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>> {
        val path by Posts.SubredditPosts(subredditName, postsSorting)
        return client.redditGet(path) {
            parameter(Keys.Time, timeSorting)
        }
    }

    override suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>> {
        val path by Posts.UserPosts(userName, postsSorting)
        return client.redditGet(path) {
            parameter(Keys.Time, timeSorting)
        }
    }

    override suspend fun upvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.UpVote
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.UnVote
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.DownVote
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    override suspend fun savePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.Save
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unSavePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.UnSave
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun hidePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.Hide
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unHidePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.UnHide
        return client.redditSubmitForm(path) {
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
    ): RedditResponse<Unit> {
        return client.submitPost(subredditName, title, isNsfw, isSpoiler, resubmit) {
            parameter(Keys.Kind, Values.Self)
            parameter(Keys.Text, text)
        }
    }


    override suspend fun submitUrlPost(
        subredditName: String,
        title: String,
        path: String,
        isNsfw: Boolean,
        isSpoiler: Boolean,
        resubmit: Boolean,
    ): RedditResponse<Unit> {
        return client.submitPost(subredditName, title, isNsfw, isSpoiler, resubmit) {
            parameter(Keys.Kind, Values.Url)
            parameter(Keys.Url, path)
        }
    }

    override suspend fun deletePost(postIdPrefixed: String): RedditResponse<Unit> {
        val path by Posts.Delete
        return client.redditSubmitForm(path) {
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
    ): RedditResponse<Unit> {
        val endpointUrl by Posts.Submit
        return client.redditSubmitForm<Map<String, Any?>>(endpointUrl) {
            parameter(Keys.Subreddit, subredditName)
            parameter(Keys.Title, title)
            parameter(Keys.Nsfw, isNsfw)
            parameter(Keys.Spoiler, isSpoiler)
            parameter(Keys.ReSubmit, resubmit)
            builder()
        }.let { response ->
            response as RedditResponse.Success
            when (response.data.getOrDefault("success", false) as Boolean) {
                true -> RedditResponse.Success(data = Unit)
                false -> RedditResponse.Failure("Something went wrong", 400)
            }
        }
    }

}