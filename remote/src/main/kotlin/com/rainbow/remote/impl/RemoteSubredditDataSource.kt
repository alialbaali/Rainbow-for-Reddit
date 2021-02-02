package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemotePostRequirements
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.impl.Endpoint.Subreddits
import com.rainbow.remote.source.RemoteSubredditDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteSubredditDataSource(client: HttpClient = mainClient): RemoteSubredditDataSource =
    RemoteSubredditDataSourceImpl(client)

private class RemoteSubredditDataSourceImpl(private val client: HttpClient) : RemoteSubredditDataSource {

    override suspend fun getSubreddit(subredditName: String): Result<RemoteSubreddit> {
        return client.get(Subreddits.About(subredditName))
    }

    override suspend fun getMySubreddits(limit: Int, after: String): Result<List<RemoteSubreddit>> {
        return client.get<Listing<RemoteSubreddit>>(Subreddits.Mine) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.mapCatching { it.toList() }
    }

    override suspend fun subscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.Subscribe) {
            parameter(Keys.SubredditName, subredditId)
            parameter(Keys.Action, Values.Sub)
        }
    }

    override suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.UnSubscribe) {
            parameter(Keys.SubredditName, subredditId)
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

    override suspend fun getSubredditSubmitText(subredditName: String): Result<String> {
        return client.plainRequest<Map<String, String>>(Subreddits.SubmitText(subredditName))
            .mapCatching { it.entries.first().value }
    }

    override suspend fun getSubredditPostRequirements(subredditName: String): Result<RemotePostRequirements> {
        return client.plainRequest(Subreddits.PostRequirements(subredditName))
    }

    override suspend fun searchSubreddit(
        subredditName: String,
        sort: String,
        limit: Int,
        after: String
    ): Result<List<RemoteSubreddit>> {
        return client.get<Listing<RemoteSubreddit>>(Subreddits.Search) {
            parameter(Keys.Query, subredditName)
            parameter(Keys.Sort, sort)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.mapCatching { it.toList() }
    }

}