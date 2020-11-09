package com.rainbow.remote

import io.ktor.http.*


sealed class RedditEndpoint(val path: String, val method: HttpMethod) {

    object Users {
        private fun routeOf(username: String) = "user/$username/about.json"

        class FindUser(username: String): RedditEndpoint(routeOf(username), HttpMethod.Get)

        class AddFriend(username: String) : RedditEndpoint(routeOf(username), HttpMethod.Post)
        class DeleteFriend(username: String) : RedditEndpoint(routeOf(username), HttpMethod.Delete)
    }

}
