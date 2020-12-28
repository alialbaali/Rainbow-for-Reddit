package com.rainbow.remote

import io.ktor.client.request.*
import io.ktor.http.*

internal fun HttpRequestBuilder.bearerAuthHeader(token: String) = header(HttpHeaders.Authorization, "Bearer $token")

internal fun HttpRequestBuilder.basicAuthHeader(credentials: String) = header(HttpHeaders.Authorization, "Basic $credentials")