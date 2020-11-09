package com.rainbow.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal suspend inline fun <reified T> HttpClient.redditRequest(
    endpoint: RedditEndpoint,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> {
    return request<HttpResponse> {
        method = endpoint.method
        url.encodedPath = endpoint.path
        builder()
    }.let { response ->
        if (response.status.isSuccess())
            response.receive<RedditResponse.Success<T>>()
                .also { println("Success Response = $it") }
        else
            response.receive<RedditResponse.Failure<T>>()
                .also { println("Failure Response = $it") }
    }
}
