package com.rainbow.remote.routes.utils

import com.rainbow.remote.redditClient
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*

internal fun MockRequestHandleScope.respondTrue(status: HttpStatusCode = HttpStatusCode.OK) = respond("true", status)

internal fun MockRequestHandleScope.respondFalse(status: HttpStatusCode = HttpStatusCode.OK) = respond("false", status)

internal fun MockRequestHandleScope.respondNotImplemented() =
    with(HttpStatusCode.NotImplemented) {
        respond(description, this)
    }

internal inline fun <reified T : Any> MockRequestHandleScope.respondJson(
    content: T,
    status: HttpStatusCode = HttpStatusCode.OK,
): HttpResponseData {

    val serializedContent = redditClient.feature(JsonFeature)
        ?.serializer
        ?.write(content, ContentType.Application.Json) as TextContent

    return respond(
        serializedContent.text,
        status,
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}