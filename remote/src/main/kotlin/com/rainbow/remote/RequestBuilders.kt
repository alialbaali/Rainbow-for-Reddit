package com.rainbow.remote

import com.rainbow.remote.impl.Endpoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal suspend inline fun <reified T> HttpClient.get(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): Result<T> = get<HttpResponse> {
    url("${endpoint.path}.json")
    builder()
}.receiveAsResponse<T>().toResult()

internal suspend inline fun <reified T> HttpClient.submitForm(
    endpoint: Endpoint,
    block: HttpRequestBuilder .() -> Unit = {},
): Result<T> = submitForm<HttpResponse> {
    url(endpoint.path)
    block()
}.receiveAsResult()

internal suspend inline fun <reified T> HttpClient.plainRequest(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): Result<T> = request<T?> {
    url("${endpoint.path}.json")
    builder()
}.asResult()

private suspend inline fun <reified T> HttpResponse.receiveAsResponse() =
    if (status.isSuccess())
        receive<Item<T>>()
    else
        try {
            receive<Error>()
        } catch (exception: NoTransformationFoundException) {
            Error(status.description, status.value)
        }

private inline fun <reified T> Response<T>.toResult(): Result<T> = when (this) {
    is Item -> Result.success(data)
    is Error -> Result.failure(Throwable("$error $message"))
}

private suspend inline fun <reified T> HttpResponse.receiveAsResult(): Result<T> =
    if (status.isSuccess())
        Result.success(receive())
    else
        Result.failure(Throwable("${receive<Any?>()}, ${status.value}"))

private inline fun <reified T> T?.asResult(): Result<T> = when (this) {
    null -> Result.failure(Throwable("Something went wrong"))
    else -> Result.success(this)
}
