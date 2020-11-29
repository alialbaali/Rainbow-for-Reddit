package com.rainbow.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend inline fun <reified T> HttpClient.redditRequest(
    url: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> = request<HttpResponse> {
    bearerAuthHeader(getLocalToken())
    url("https://oauth.reddit.com/$url.json")
    builder()
}.asRedditResponse()


suspend inline fun <reified T> HttpClient.customRedditRequest(
    url: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): T? = request {
    bearerAuthHeader(getLocalToken())
    url("https://oauth.reddit.com/$url.json")
    builder()
}


suspend inline fun <reified T> HttpClient.redditSubmitForm(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = submitForm {
    bearerAuthHeader(getLocalToken())
    url("https://oauth.reddit.com/$url")
    block()
}

suspend inline fun <reified T> HttpClient.redditGet(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> = redditRequest(urlString) {
    method = HttpMethod.Get
    builder()
}

suspend inline fun <reified T> HttpClient.redditPost(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> = redditRequest(urlString) {
    method = HttpMethod.Post
    builder()
}

suspend inline fun <reified T> HttpClient.redditPut(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> = redditRequest(urlString) {
    method = HttpMethod.Put
    builder()
}

suspend inline fun <reified T> HttpClient.redditDelete(
    urlString: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): RedditResponse<T> = redditRequest(urlString) {
    method = HttpMethod.Delete
    builder()
}

suspend inline fun <reified T> HttpResponse.asRedditResponse(): RedditResponse<T> =
    if (status.isSuccess())
        receive<RedditResponse.Success<T>>()
    else
        receive<RedditResponse.Failure<T>>()


fun getLocalToken() = "383986809160-W8fkmrvNV_VrDouPMun37hc9J20Mtg"
