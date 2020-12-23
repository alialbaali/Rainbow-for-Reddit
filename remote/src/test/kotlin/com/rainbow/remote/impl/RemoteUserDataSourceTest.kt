package com.rainbow.remote.impl

import com.rainbow.remote.testClient
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.http.*

private class RemoteUserDataSourceTest : StringSpec() {

    private val source = RemoteUserDataSource(testClient)

    init {

        "Get valid user information" {
            source.getUserAbout("test")
                .shouldBeSuccess()
                .data
                .name
                .shouldNotBeBlank()
        }

        "Get invalid user information" {
            source.getUserAbout("invalid")
                .shouldBeFailure()
                .error shouldBeExactly HttpStatusCode.NotFound.value
        }

        "Check an available username" {
            source.checkUserName("test")
                .shouldBeSuccess()
                .data
                .shouldBeTrue()
        }

        "Check a non-available username" {
            source.checkUserName("failure")
                .shouldBeSuccess()
                .data
                .shouldBeFalse()
        }

    }

}