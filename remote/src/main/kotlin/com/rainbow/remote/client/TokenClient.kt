package com.rainbow.remote.client

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.http.*

private const val ClientId = "cpKMrRbh8b06TQ"
private const val ClientPassword = ""

internal val tokenClient by lazy {
    HttpClient(Apache) {
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
