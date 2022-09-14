package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.impl.Endpoint.Posts
import com.rainbow.remote.source.RemotePostDataSource
import io.ktor.client.*
import io.ktor.client.request.*

class RemotePostDataSourceImpl(private val client: HttpClient = redditClient) : RemotePostDataSource {

    override suspend fun getHomePosts(
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.MainPagePosts(postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.SubredditPosts(subredditName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getUserSubmittedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.UserSubmittedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getUserUpvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.UserUpvotedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getUserDownvotedPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.UserDownvotedPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getUserHiddenPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.UserHiddenPosts(userName, postsSorting)) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, after)
            parameter(Keys.Limit, limit)
        }.toList()
    }

    override suspend fun getPost(postId: String): RemotePost {
        return client.getOrThrow<Listing<RemotePost>>(Posts.GetPost) {
            parameter(Keys.Id, postId)
        }.toList().single()
    }

    override suspend fun followPost(postId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun unFollowPost(postId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun upvotePost(postId: String) {
        return client.submitFormOrThrow(Posts.Upvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvotePost(postId: String) {
        return client.submitFormOrThrow(Posts.Unvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvotePost(postId: String) {
        return client.submitFormOrThrow(Posts.Downvote) {
            parameter(Keys.Id, postId)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    override suspend fun savePost(postId: String) {
        return client.submitFormOrThrow(Posts.Save) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun unSavePost(postId: String) {
        return client.submitFormOrThrow(Posts.UnSave) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun hidePost(postId: String) {
        return client.submitFormOrThrow(Posts.Hide) {
            parameter(Keys.Id, postId)
        }
    }

    override suspend fun unHidePost(postId: String) {
        return client.submitFormOrThrow(Posts.UnHide) {
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

    override suspend fun searchPosts(
        searchTerm: String,
        postsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemotePost> {
        return client.getOrThrow<Listing<RemotePost>>(Posts.Search) {
            parameter(Keys.Query, searchTerm)
            parameter(Keys.Sort, postsSorting)
            parameter(Keys.Time, timeSorting)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
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