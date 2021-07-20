package com.rainbow.remote

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*


internal val testClient by lazy {
    HttpClient(MockEngine) {

        defaultRequest {
            expectSuccess = false
        }

        Logging {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }

        Json {
            serializer = KotlinxSerializer()
            accept(ContentType.Application.Json)
        }

        engine {
            redditHandler()
        }

    }
}
