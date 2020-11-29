package com.rainbow.remote

import com.rainbow.remote.dto.TokenResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*

private const val GrantTypeKey = "grant_type"
private const val RefreshTokenKey = "refresh_token"
private const val BasicAuthCredentials = "Y3BLTXJSYmg4YjA2VFE6"

internal class RefreshToken(val config: Config) {

    internal companion object : HttpClientFeature<Config, RefreshToken> {

        override val key: AttributeKey<RefreshToken> = AttributeKey("RefreshToken")

        override fun prepare(block: Config.() -> Unit): RefreshToken {
            return RefreshToken(Config().apply(block))
        }

        override fun install(feature: RefreshToken, scope: HttpClient) =
            scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->

                if (response.status == HttpStatusCode.Unauthorized)
                    with(scope.refreshToken(feature.config)) {

                        val newResponse = scope.request<HttpResponse> {
                            takeFrom(response.request)
                            headers.remove(HttpHeaders.Authorization)
                            accessToken?.let { bearerAuthHeader(accessToken) }
                        }

                        proceedWith(newResponse)
                    }
            }

        private suspend fun HttpClient.refreshToken(config: Config): TokenResponse {
            return submitForm(config.uri) {
                basicAuthHeader(BasicAuthCredentials)
                parameter(GrantTypeKey, config.grantType)
                parameter(RefreshTokenKey, config.refreshToken)
            }
        }

    }

    internal class Config {

        lateinit var uri: String

        lateinit var refreshToken: String

        var grantType: String = RefreshTokenKey

    }

}

internal fun HttpClientConfig<*>.RefreshToken(block: RefreshToken.Config.() -> Unit) = install(RefreshToken, block)
