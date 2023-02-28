package com.rainbow.remote

import com.rainbow.remote.client.Clients
import com.rainbow.remote.client.settings
import com.rainbow.remote.dto.TokenResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

private const val GrantTypeKey = "grant_type"
private const val RefreshTokenKey = "refresh_token"
private const val AccessTokenKey = "access_token"

internal object TokenHandler {

    private val client = Clients.Token

    private val accessToken: String
        get() = settings.getStringOrNull(AccessTokenKey)!!

    private val refreshToken: String
        get() = settings.getStringOrNull(RefreshTokenKey)!!

    suspend fun loadToken(): BearerTokens {
        requestToken()?.let {
            setAccessToken(it.accessToken)
            setRefreshToken(it.refreshToken)
        }
        return BearerTokens(accessToken, refreshToken)
    }

    suspend fun loadRefreshToken(): BearerTokens {
        requestRefreshToken(refreshToken)?.let {
            setAccessToken(it.accessToken)
            setRefreshToken(it.refreshToken)
        }
        return BearerTokens(accessToken, refreshToken)
    }

    private suspend fun requestToken(): TokenResponse? {
        val parameters = ParametersBuilder().apply {
            append(GrantTypeKey, "authorization_code")
            append("redirect_uri", RainbowProperties.RainbowUrl)
            append("code", settings.getStringOrNull("code")!!)
        }.build()
        val response = client.submitForm(parameters)
        return if (response.status.isSuccess()) {
            response.body()
        } else {
            null
        }
    }

    private suspend fun requestRefreshToken(refreshToken: String?): TokenResponse? {
        val response = client.submitForm {
            parameter(GrantTypeKey, "refresh_token")
            parameter(RefreshTokenKey, refreshToken)
        }
        return if (response.status.isSuccess()) {
            response.body()
        } else {
            null
        }
    }

    private fun setAccessToken(accessToken: String) = settings.putString(AccessTokenKey, accessToken)

    private fun setRefreshToken(refreshToken: String) = settings.putString(RefreshTokenKey, refreshToken)
}