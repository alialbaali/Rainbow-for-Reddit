package com.rainbow.remote

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.Json as JsonConfig

private const val ClientId = "cpKMrRbh8b06TQ"
private const val ClientPassword = ""
private const val RefreshTokenUri = "https://www.reddit.com/api/v1/access_token"
private const val CurrentRefreshToken = "383986809160-zpP5sfAA_VWFJcbII6rp3WIUWf2onQ"
private const val UploadUri = "https://reddit-uploaded-media.s3-accelerate.amazonaws.com/"
private const val isSignedIn = true

internal val mainClient by lazy {
    HttpClient(Apache) {

        if (isSignedIn)
            RefreshToken {
                uri = RefreshTokenUri
                refreshToken = CurrentRefreshToken
            }

        defaultRequest {
            expectSuccess = false
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
                }
            )

            accept(ContentType.Application.Json)
        }

        UserAgent {
            agent = "com.test.app"
        }

    }
}

suspend fun downloadImage(url: String) = mainClient.get<HttpResponse>(url).content.toByteArray()

internal fun HttpClientConfig<*>.UserAgent(block: UserAgent.Config.() -> Unit) = install(UserAgent, block)
