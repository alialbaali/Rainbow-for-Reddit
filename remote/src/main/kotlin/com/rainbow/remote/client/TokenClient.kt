package com.rainbow.remote.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.json.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.http.*

private const val ClientId = "cpKMrRbh8b06TQ"
private const val ClientPassword = ""

internal val tokenClient by lazy {
    HttpClient(CIO) {
        developmentMode = true
        expectSuccess = false

        ResponseObserver {
            println(it)
        }

        Logging {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }

        Auth {
            basic {
                sendWithoutRequest { true }
                credentials { BasicAuthCredentials(ClientId, ClientPassword) }
            }
        }

        Json {
            serializer = DefaultSerializer
            accept(ContentType.Application.Json)
        }

        install(UserAgent) {
            agent = RainbowUserAgent
        }
    }
}
