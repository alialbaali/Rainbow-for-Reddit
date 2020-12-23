package com.rainbow.remote.impl

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.testClient
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf

private class RemoteUserDataSource : StringSpec() {

    private val source = RemoteUserDataSource(testClient)

    init {

        "Get user about information" {
            source.getUserAbout("test")
                .shouldBeInstanceOf<RedditResponse.Success<RemoteUser>>()
                .let { it as RedditResponse.Success<RemoteUser> }
                .data
                .name
                .shouldNotBeBlank()
        }


        "Check username availability" {
            source.checkUserName("test")
                .shouldBeInstanceOf<RedditResponse.Success<Boolean>>()
                .let { it as RedditResponse.Success<Boolean> }
                .data
                .shouldBeTrue()
        }

    }


}