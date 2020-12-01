package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.client
import com.rainbow.remote.customRedditRequest
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.redditGet
import com.rainbow.remote.source.RemoteUserDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteUserDataSource(): RemoteUserDataSource = UserDataSourceImpl(client)

private class UserDataSourceImpl(private val client: HttpClient) : RemoteUserDataSource {

    override suspend fun getUserAbout(userName: String): RedditResponse<RemoteUser> {
        val url = "user/$userName/about"
        return client.redditGet(url)
    }

    override suspend fun checkUserName(userName: String): RedditResponse<Boolean> {
        val url = "api/username_available"
        val response = client.customRedditRequest<Boolean>(url) {
            parameter(Keys.User, userName)
        }

        return when (response) {
            null -> RedditResponse.Failure("Error", 400)
            else -> RedditResponse.Success(data = response)
        }
    }
}