package com.rainbow.remote.client

import com.rainbow.remote.dto.TokenResponse
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

private const val RefreshTokenUrl = "https://www.reddit.com/api/v1/access_token"
private const val UploadUri = "https://reddit-uploaded-media.s3-accelerate.amazonaws.com/"
private const val GrantTypeKey = "grant_type"
private const val RefreshTokenKey = "refresh_token"
private const val RedirectUrl = "http://localhost:8080"
private const val AccessTokenKey = "access_token"

internal val redditClient by lazy {
    HttpClient(Apache) {
        developmentMode = true
        expectSuccess = false

        ResponseObserver {
            println(it)
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "oauth.reddit.com"
            }
        }

        Logging {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }

        Auth {
            bearer {
                loadTokens {
                    runCatching { tokenClient.loadToken() }
                        .getOrNull()
                        ?.let {
                            settings.putAccessToken(it.accessToken)
                            settings.putRefreshToken(it.refreshToken)
                        }
                    BearerTokens(settings.accessToken, settings.refreshToken)
                }

                refreshTokens {
                    runCatching { tokenClient.refreshToken(settings.refreshToken) }
                        .getOrNull()
                        ?.let {
                            settings.putAccessToken(it.accessToken)
                            settings.putRefreshToken(it.refreshToken)
                        }
                    BearerTokens(settings.accessToken, settings.refreshToken)
                }
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

private suspend fun HttpClient.loadToken(): TokenResponse {
    val formParameters = ParametersBuilder().apply {
        append(GrantTypeKey, "authorization_code")
        append("redirect_uri", RedirectUrl)
        append("code", settings.getString("code"))
    }.build()
    return submitForm(RefreshTokenUrl, formParameters)
}

private suspend fun HttpClient.refreshToken(refreshToken: String?): TokenResponse = submitForm(RefreshTokenUrl) {
    parameter(GrantTypeKey, "refresh_token")
    parameter(RefreshTokenKey, refreshToken)
}

private fun Settings.putRefreshToken(refreshToken: String) = putString(RefreshTokenKey, refreshToken)

private fun Settings.putAccessToken(accessToken: String) = putString(AccessTokenKey, accessToken)

private val Settings.accessToken: String
    get() = getString(AccessTokenKey)

private val Settings.refreshToken: String
    get() = getString(RefreshTokenKey)

