package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.client.Clients
import com.rainbow.remote.client.settings
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.dto.Scope
import com.rainbow.remote.dto.Scope.Companion.joinToLowerCaseString
import com.rainbow.remote.impl.Endpoint.Users
import com.rainbow.remote.source.RemoteUserDataSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

private const val RedditAuthUrl = "www.reddit.com/api/v1/authorize"

class RemoteUserDataSourceImpl(
    private val rainbowClient: HttpClient = Clients.Rainbow,
    private val redditClient: HttpClient = Clients.Reddit,
) : RemoteUserDataSource {

    override fun createAuthenticationUrl(uuid: UUID): String {
        return URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = RedditAuthUrl,
            parameters = ParametersBuilder().apply {
                append("client_id", RainbowProperties.ClientId)
                append("response_type", "code")
                append("state", uuid.toString())
                append("redirect_uri", RainbowProperties.RainbowUrl)
                append("duration", "permanent")
                append("scope", Scope.UserScope.joinToLowerCaseString())
            }.build()
        ).buildString()
    }

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

    override suspend fun getCurrentUser(): RemoteUser {
        return redditClient.requestOrThrow(Users.CurrentUser)
    }

    override suspend fun getUserAbout(userName: String): RemoteUser {
        return redditClient.getOrThrow(Users.About(userName))
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

    override suspend fun searchUsers(searchTerm: String, limit: Int, after: String?): List<RemoteUser> {
        return redditClient.getOrThrow<Listing<RemoteUser>>(Users.Search) {
            parameter(Keys.Query, searchTerm)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }
}