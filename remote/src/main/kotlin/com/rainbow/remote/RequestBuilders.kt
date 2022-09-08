package com.rainbow.remote

import com.rainbow.remote.impl.Endpoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal suspend inline fun <reified T> HttpClient.getOrThrow(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): T {
    val response = get {
        url("${endpoint.path}.json")
        builder()
    }
    return if (response.status.isSuccess()) {
        response.body<Item<T>>().data
    } else {
        val error = try {
            response.body()
        } catch (exception: Throwable) {
            Error(response.status.description, response.status.value)
        }
        error(error.message.toString())
    }
}

internal suspend inline fun <reified T> HttpClient.requestOrThrow(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): T {
    val response = request {
        url("${endpoint.path}.json")
        builder()
    }
    return if (response.status.isSuccess()) {
        response.body()
    } else {
        val error = try {
            response.body()
        } catch (exception: Throwable) {
            Error(response.status.description, response.status.value)
        }
        error(error.message.toString())
    }
}

internal suspend inline fun <reified T> HttpClient.submitFormOrThrow(
    endpoint: Endpoint,
    block: HttpRequestBuilder .() -> Unit = {},
): T {
    val response = submitForm {
        url(endpoint.path)
        block()
    }
    return if (response.status.isSuccess()) {
        response.body()
    } else {
        val error = try {
            response.body()
        } catch (exception: Throwable) {
            Error(response.status.description, response.status.value)
        }
        error(error.message.toString())
    }
}

internal suspend inline fun <reified T> HttpClient.get(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): Result<T> = get {
    url("${endpoint.path}.json")
    builder()
}.receiveAsResponse<T>().toResult()

internal suspend inline fun <reified T> HttpClient.submitForm(
    endpoint: Endpoint,
    block: HttpRequestBuilder .() -> Unit = {},
): Result<T> = submitForm {
    url(endpoint.path)
    block()
}.receiveAsResult()

internal suspend inline fun <reified T> HttpClient.plainRequest(
    endpoint: Endpoint,
    builder: HttpRequestBuilder.() -> Unit = {},
): Result<T> = request {
    url("${endpoint.path}.json")
    builder()
}.receiveAsResult()

private suspend inline fun <reified T> HttpResponse.receiveAsResponse() =
    if (status.isSuccess())
        body<Item<T>>()
    else
        try {
            body<Error>()
        } catch (exception: NoTransformationFoundException) {
            Error(status.description, status.value)
        }

private inline fun <reified T> Response<T>.toResult(): Result<T> = when (this) {
    is Item -> Result.success(data)
    is Error -> Result.failure(Throwable("$error $message"))
}

private suspend inline fun <reified T> HttpResponse.receiveAsResult(): Result<T> =
    if (status.isSuccess())
        Result.success(body())
    else
        Result.failure(Throwable("${status.description}, ${status.value}"))