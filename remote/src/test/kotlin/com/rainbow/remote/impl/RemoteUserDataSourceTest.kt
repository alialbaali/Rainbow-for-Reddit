package com.rainbow.remote.impl

import com.rainbow.remote.testClient
import io.kotest.core.spec.autoClose
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.string.shouldContain
import io.ktor.http.*

private class RemoteUserDataSourceTest : StringSpec() {

    private val source = RemoteUserDataSource(autoClose(testClient))

    init {

        "Get valid user information" {

            val y = source.getUserAbout("test")
                .shouldBeSuccess {
                    it!!.name shouldBeEqualIgnoringCase "test"
                }
        }

        "Get invalid user information" {
            source.getUserAbout("invalid")
                .shouldBeFailure {
                    it!!.message shouldContain HttpStatusCode.NotFound.value.toString()
                }

        }

        "Check an available username" {

            source.checkUserName("test")
                .shouldBeSuccess {
                    it!!.shouldBeTrue()
                }


        }

        "Check a non-available username" {

            source.checkUserName("failure")
                .shouldBeSuccess {
                    it!!.shouldBeFalse()
                }

        }

    }

}