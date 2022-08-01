package com.rainbow.remote.client

import com.rainbow.remote.dto.TokenResponse
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.json.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

private const val RefreshTokenUrl = "https://www.reddit.com/api/v1/access_token"
private const val UploadUri = "https://reddit-uploaded-media.s3-accelerate.amazonaws.com/"
private const val GrantTypeKey = "grant_type"
private const val RefreshTokenKey = "refresh_token"
private const val RedirectUrl = "https://rainbowforreddit.herokuapp.com/"
private const val AccessTokenKey = "access_token"

internal val redditClient by lazy {
    HttpClient(CIO) {
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
    return submitForm(RefreshTokenUrl, formParameters).body()
}

private suspend fun HttpClient.refreshToken(refreshToken: String?): TokenResponse =
    submitForm(RefreshTokenUrl) {
        parameter(GrantTypeKey, "refresh_token")
        parameter(RefreshTokenKey, refreshToken)
    }.body()

private fun Settings.putRefreshToken(refreshToken: String) =
    putString(RefreshTokenKey, refreshToken)

private fun Settings.putAccessToken(accessToken: String) = putString(AccessTokenKey, accessToken)

private val Settings.accessToken: String
    get() = getString(AccessTokenKey)

private val Settings.refreshToken: String
    get() = getString(RefreshTokenKey)

