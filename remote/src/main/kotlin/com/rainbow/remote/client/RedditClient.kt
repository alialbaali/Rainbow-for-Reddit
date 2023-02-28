package com.rainbow.remote.client

import com.rainbow.remote.TokenHandler
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*

private const val RedditHost = "oauth.reddit.com"

internal val redditClient by lazy {
    HttpClient(DefaultEngine) {
        applyDefaultConfig()

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = RedditHost
            }
        }

        Auth {
            bearer {
                loadTokens { TokenHandler.loadToken() }
                refreshTokens { TokenHandler.loadRefreshToken() }
            }
        }
    }
}