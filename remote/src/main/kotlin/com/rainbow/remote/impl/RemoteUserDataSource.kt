package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteUserDataSource(client: HttpClient = mainClient): RemoteUserDataSource = RemoteUserDataSourceImpl(client)

private class RemoteUserDataSourceImpl(private val client: HttpClient) : RemoteUserDataSource {

    override suspend fun getUserAbout(userName: String): RedditResponse<RemoteUser> {
        val path by Endpoint.Users.About(userName)
        return client.redditGet(path)
    }

    override suspend fun checkUserName(userName: String): RedditResponse<Boolean> {
        val path by Endpoint.Users.CheckUserName
        val response = client.customRedditRequest<Boolean>(path) {
            parameter(Keys.User, userName)
        }

        return when (response) {
            null -> RedditResponse.Failure("Error", 400)
            else -> RedditResponse.Success(data = response)
        }
    }
}