package com.rainbow.remote.client

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal typealias DefaultEngine = CIO

internal val settings = Settings()

internal val DefaultJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}

internal fun HttpClientConfig<*>.applyDefaultConfig() {
    developmentMode = true
    expectSuccess = false
    ResponseObserver(::println)
    Logging {
        level = LogLevel.ALL
        logger = Logger.DEFAULT
    }
    install(ContentNegotiation) {
        json(DefaultJson)
    }
}