package com.rainbow.remote.client

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.http.*

internal val rainbowClient by lazy {
    HttpClient(Apache) {
        developmentMode = true
        expectSuccess = false

        engine {
            socketTimeout = 0
        }

        ResponseObserver {
            println(it)
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "rainbowforreddit.herokuapp.com"
            }
        }

        Logging {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }

        Json {
            serializer = DefaultSerializer
            accept(ContentType.Application.Json)
        }
    }
}