package com.rainbow.remote.client

import com.rainbow.remote.RainbowProperties
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

private const val RefreshTokenUrl = "https://www.reddit.com/api/v1/access_token"

internal val tokenClient by lazy {
    HttpClient(DefaultEngine) {
        applyDefaultConfig()

        defaultRequest {
            url(RefreshTokenUrl)
        }

        Auth {
            basic {
                sendWithoutRequest { true }
                credentials { BasicAuthCredentials(RainbowProperties.ClientId, RainbowProperties.ClientPassword) }
            }
        }
    }
}