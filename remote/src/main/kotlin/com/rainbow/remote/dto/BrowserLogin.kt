package com.rainbow.remote.dto

import com.rainbow.remote.dto.Scope.Companion.joinToLowerCaseString
import com.rainbow.remote.mainClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

private const val CodeKey = "code"
private const val ClientId = "cpKMrRbh8b06TQ"
private const val GrantTypeKey = "grant_type"
private const val AuthorizationCode = "authorization_code"
private const val RedirectUri = "http://localhost:8080"
private const val RedirectUriKey = "redirect_uri"
private const val AccessTokenUri = "https://www.reddit.com/api/v1/access_token"

private const val ClientIdKey = "client_id"
private const val ResponseTypeKey = "response_type"
private const val CodeResponseType = "code"
private const val StateKey = "state"
private const val StateValue = "StateValue"
private const val DurationKey = "duration"
private const val PermanentDuration = "permanent"
private const val ScopeKey = "scope"

private val AuthUrl = request {
    url("https://www.reddit.com/api/v1/authorize")
    parameter(ClientIdKey, ClientId)
    parameter(ResponseTypeKey, CodeResponseType)
    parameter(StateKey, StateValue)
    parameter(RedirectUriKey, RedirectUri)
    parameter(DurationKey, PermanentDuration)
    parameter(ScopeKey, Scope.UserScope.joinToLowerCaseString())
}


private suspend fun HttpClient.getAccessToken(code: String): TokenResponse {
    return submitForm(AccessTokenUri) {
        parameter(GrantTypeKey, AuthorizationCode)
        parameter(RedirectUriKey, RedirectUri)
        parameter(CodeKey, code)
    }
}

private suspend fun main() {

    val driver = setUpWebDriver().apply {
        manage()
            .window()
            .maximize()

        navigate().to(
            AuthUrl
                .build()
                .url
                .toURI()
                .toURL()
        )
    }
    while (true) {
        delay(5000)
        if (driver.currentUrl.startsWith("http://localhost:8080")) {
            val uri = driver.currentUrl.also(::println)
            val code = getCodeFromUri(uri).also(::println)
            mainClient.getAccessToken(code)
                .also(::println)
        }
    }

}

private fun getCodeFromUri(uri: String): String = uri.substringAfter("code=")

private fun setUpWebDriver(): WebDriver {
    System.setProperty("webdriver.gecko.driver", "C:\\Users\\ali\\Downloads\\geckodriver\\geckodriver.exe")
    return FirefoxDriver()
}