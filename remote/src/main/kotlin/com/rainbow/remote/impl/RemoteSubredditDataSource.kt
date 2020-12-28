package com.rainbow.remote.impl

import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.impl.Endpoint.Subreddits
import com.rainbow.remote.mainClient
import com.rainbow.remote.get
import com.rainbow.remote.source.RemoteSubredditDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteSubredditDataSource(client: HttpClient = mainClient): RemoteSubredditDataSource =
    RemoteSubredditDataSourceImpl(client)

private class RemoteSubredditDataSourceImpl(private val client: HttpClient) : RemoteSubredditDataSource {

    override suspend fun getSubredditAbout(subredditName: String): Result<RemoteSubreddit> {
        return client.get(Subreddits.About(subredditName))
    }

    override suspend fun getMySubreddits(): Result<List<RemoteSubreddit>> {
        return client.get(Subreddits.Mine)
    }

    override suspend fun subscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.Subscribe) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.Sub)
        }
    }

    override suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.UnSubscribe) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.unSub)
        }
    }

    override suspend fun favoriteSubreddit(subredditName: String): Result<Unit> {
        return client.submitForm(Subreddits.Favorite) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, true)
        }
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit> {
        return client.submitForm(Subreddits.UnFavorite) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, false)
        }
    }

}