package com.rainbow.remote.client

import com.rainbow.remote.RainbowProperties
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.http.*

internal val rainbowClient by lazy {
    HttpClient(DefaultEngine) {
        applyDefaultConfig()

        engine {
            requestTimeout = 0 // Disabled to wait for user login response.
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = RainbowProperties.RainbowHost
            }
        }
    }
}