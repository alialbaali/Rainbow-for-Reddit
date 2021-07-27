package com.rainbow.remote

import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.dto.Thing
import com.rainbow.remote.dto.TokenResponse
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.observer.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File
import kotlinx.serialization.json.Json as JsonConfig

private const val ClientId = "cpKMrRbh8b06TQ"
private const val ClientPassword = ""
private const val RefreshTokenUri = "https://www.reddit.com/api/v1/access_token"
private const val UploadUri = "https://reddit-uploaded-media.s3-accelerate.amazonaws.com/"
private const val TokenResponsePath = "/home/alialbaali/Projects/Rainbow/TokenResponse.json"
private const val isSignedIn = true

internal val mainClient by lazy {
    HttpClient(Apache) {

        developmentMode = true
        expectSuccess = false

        ResponseObserver {
            println(it)
        }

        if (isSignedIn)
            RefreshToken {
                uri = RefreshTokenUri
                refreshToken = getTokenResponse().getOrThrow().refreshToken!!
                onNewTokenResponse = { tokenResponse ->
                    setTokenResponse(tokenResponse)
                }
            }

//        Logging {
//            level = LogLevel.ALL
//            logger = Logger.DEFAULT
//        }

        Auth {
            basic {
                username = ClientId
                password = ClientPassword
            }
        }

        Json {
            serializer = KotlinxSerializer(
                JsonConfig {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    serializersModule = SerializersModule {
                        polymorphic(Thing::class) {
                            subclass(RemotePost::class, RemotePost.serializer())
                            subclass(RemoteComment::class, RemoteComment.serializer())
//                            default { RemoteComment.serializer() }
                        }
                    }
                }
            )

            accept(ContentType.Application.Json)
        }

        UserAgent {
            agent = "com.test.app"
        }

    }
}

internal fun HttpClientConfig<*>.UserAgent(block: UserAgent.Config.() -> Unit) = install(UserAgent, block)

private val RainbowUserAgent = "${Platform.current.id}:com.rainbow:v0.0.1 (by /u/EnigmaGram)"

internal fun getTokenResponse(): Result<TokenResponse> = runCatching {
    File(TokenResponsePath)
        .readText()
        .let { JsonConfig.decodeFromString(it) }
}

internal fun setTokenResponse(tokenResponse: TokenResponse) = runCatching {
    File(TokenResponsePath)
        .writeText(JsonConfig.encodeToString(tokenResponse))
}