package com.rainbow.remote.impl

import com.rainbow.remote.testClient
import io.kotest.core.spec.autoClose
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.string.shouldBeEqualIgnoringCase

private class RemoteSubredditDataSourceTest : StringSpec() {

    private val source = RemoteSubredditDataSource(autoClose(testClient))

    init {

        "Get valid subreddit information" {

            source.getSubreddit("Kotlin")
                .shouldBeSuccess {
                    it!!.displayName shouldBeEqualIgnoringCase "Kotlin"
                }

        }

        "Get my Subreddits" {

//            source.getMySubreddits()
//                .shouldBeSuccess {
//                    it!!.count() shouldBeExactly 10
//                }

        }

    }

}