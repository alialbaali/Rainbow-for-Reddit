package com.rainbow.remote.routes

import com.rainbow.remote.Kind
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.impl.Endpoint.Subreddits
import com.rainbow.remote.routes.utils.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

@OptIn(ExperimentalStdlibApi::class)
internal fun MockRequestHandleScope.subredditRoute(request: HttpRequestData) = when (request.urlPath) {
    Subreddits.About("Kotlin").jsonPath -> respondItem(Kind.Subreddit, TestSubreddit)
    Subreddits.About("notFound").jsonPath -> respondError("Subreddit Not Found", HttpStatusCode.NotFound)
    Subreddits.Favorite.jsonPath -> {
        if (request.queryParams["favorite"].toBoolean())
            respondItem(null, null)
        else
            respondItem(null, null)
    }
    Subreddits.UnFavorite.jsonPath -> respondItem(null, null)
    Subreddits.Subscribe.jsonPath -> respondItem(null, null)
    Subreddits.UnSubscribe.jsonPath -> respondItem(null, null)
    Subreddits.Mine.jsonPath -> {
        respondItem(
            kind = Kind.Listing,
            data = buildList {
                repeat(10) {
                    add(TestSubreddit)
                }
            }
        )
    }
    else -> null
}

internal val TestSubreddit = RemoteSubreddit(
    id = "id",
    name = "name_id",
    title = "Kotlin Programming Language",
    over18 = false,
    displayName = "Kotlin",
    publicDescription = "Discussion about Kotlin, a statically typed programming language for the JVM, Android, JavaScript, and native.",
    submitText = "",
    description = "Discussion about Kotlin, a statically typed programming language for the JVM, Android, JavaScript, and native.",
    subscribers = 42900,
    url = "https://www.reddit.com/r/Kotlin/",
    primaryColor = "FFFFFF",
    keyColor = "000000",
    bannerBackgroundColor = "FF00FF",
    communityIcon = "https://styles.redditmedia.com/t5_2so2r/styles/communityIcon_p0gr6t26jae41.png",
    bannerBackgroundImage = "https://styles.redditmedia.com/t5_2so2r/styles/communityIcon_p0gr6t26jae41.png",
    lang = "en",
    activeUserCount = 131,
    userHasFavorited = true,
    userIsContributor = false,
    userIsSubscriber = true,
    userIsModerator = false,
    created = null,
    createdUtc = null,
)
