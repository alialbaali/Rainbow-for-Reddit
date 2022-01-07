package com.rainbow.remote.client

import com.rainbow.remote.Platform
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

internal val RainbowUserAgent = "${Platform.current.id}:com.rainbow:v0.0.1 (by /u/EnigmaGram)"

@OptIn(ExperimentalSettingsImplementation::class)
internal val settings = Settings()

internal val DefaultSerializer = KotlinxSerializer(
    Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
)