package com.rainbow.remote.client

import com.rainbow.remote.Platform
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.dto.Thing
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val RainbowUserAgent = "${Platform.current.id}:com.rainbow:v0.0.1 (by /u/EnigmaGram)"

@OptIn(ExperimentalSettingsImplementation::class)
internal val settings = JvmPreferencesSettings.Factory()
    .create(null)

internal val DefaultSerializer = KotlinxSerializer(
    Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        serializersModule = SerializersModule {
            polymorphic(Thing::class) {
                subclass(RemotePost::class, RemotePost.serializer())
                subclass(RemoteComment::class, RemoteComment.serializer())
//                default { RemoteComment.serializer() }
            }
        }
    }
)