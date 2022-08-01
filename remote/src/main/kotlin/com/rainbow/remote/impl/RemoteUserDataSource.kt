package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.Listing
import com.rainbow.remote.client.settings
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.get
import com.rainbow.remote.impl.Endpoint.Users
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteUserDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.*

fun RemoteUserDataSource(
    rainbowClient: HttpClient = com.rainbow.remote.client.rainbowClient,
    redditClient: HttpClient = com.rainbow.remote.client.redditClient,
): RemoteUserDataSource = RemoteUserDataSourceImpl(rainbowClient, redditClient)

private class RemoteUserDataSourceImpl(
    private val rainbowClient: HttpClient,
    private val redditClient: HttpClient,
) : RemoteUserDataSource {

    override suspend fun loginUser(uuid: UUID): Result<Unit> {
        val response = rainbowClient.get("/code") {
            parameter("id", uuid.toString())
        }.body<Map<String, String?>>()
        return when (val code = response["code"]) {
            null -> Result.failure(Throwable("Login failed"))
            else -> {
                settings.putString("code", code)
                Result.success(Unit)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<RemoteUser> {
        return redditClient.plainRequest(Users.CurrentUser)
    }

    override suspend fun getUserAbout(userName: String): Result<RemoteUser> {
        return redditClient.get(Users.About(userName))
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> {
        return redditClient.plainRequest<String>(Users.CheckUserName) {
            parameter(Keys.User, userName)
        }.mapCatching { it.toBoolean() }
    }

    override suspend fun blockUser(userName: String): Result<Unit> {
        return redditClient.submitForm(Users.BlockUser) {
            parameter(Keys.Name, userName)
        }
    }

    override suspend fun reportUser(userName: String, reason: String): Result<Unit> {
        return redditClient.submitForm(Users.ReportUser) {
            parameter(Keys.User, userName)
            parameter(Keys.Reason, reason)
        }
    }

    override suspend fun searchUsers(searchTerm: String, limit: Int, after: String?): Result<List<RemoteUser>> {
        return redditClient.get<Listing<RemoteUser>>(Users.Search) {
            parameter(Keys.Query, searchTerm)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.mapCatching { it.toList() }
    }
}