package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.testClient
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf

private class RemoteUserDataSourceTest : StringSpec() {

    private val source = RemoteUserDataSource(testClient)

    init {

        "Fun" {
            source.getUserAbout("test")
                .shouldBeInstanceOf<RedditResponse.Success<RemoteUser>>()


        }


    }

}