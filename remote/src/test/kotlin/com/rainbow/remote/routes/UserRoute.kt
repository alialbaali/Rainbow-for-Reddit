package com.rainbow.remote.routes

import com.rainbow.remote.Kind
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.impl.Endpoint.Users
import com.rainbow.remote.routes.utils.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

internal fun MockRequestHandleScope.userRoute(request: HttpRequestData): HttpResponseData? = when (request.urlPath) {
    Users.About("test").jsonPath -> respondItem(Kind.User, TestUser)
    Users.About("invalid").jsonPath -> respondError("User Not Found", HttpStatusCode.NotFound)
    Users.CheckUserName.jsonPath -> {
        if (request.queryParams["user"] == "test")
            respondTrue()
        else
            respondFalse()
    }
    else -> null
}

private val TestUser = RemoteUser(
    id = "",
    name = "test",
    linkKarma = 23,
    commentKarma = 342,
    iconImg = "",
    totalKarma = 0,
    verified = false,
    isGold = false,
    isMod = true,
//    subreddit = TestSubreddit,
    created = 0.0,
    createdUtc = 0.0,
)