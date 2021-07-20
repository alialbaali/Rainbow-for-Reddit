package com.rainbow.remote.routes.utils

import com.rainbow.remote.impl.Endpoint
import io.ktor.client.request.*
import io.ktor.http.*


internal val HttpRequestData.urlPath
    get() = url.encodedPath.removePrefix("/")

internal val HttpRequestData.queryParams
    get() = url.parameters

internal val Endpoint.jsonPath
    get() = "$path.json"

internal val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort

internal val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

internal val Url.fullHost: String get() = "${protocol.name}://$hostWithPortIfRequired/"
