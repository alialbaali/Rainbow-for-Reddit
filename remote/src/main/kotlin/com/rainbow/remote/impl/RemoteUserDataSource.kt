package com.rainbow.remote.impl

import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.get
import com.rainbow.remote.mainClient
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteUserDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteUserDataSource(client: HttpClient = mainClient): RemoteUserDataSource = RemoteUserDataSourceImpl(client)

private class RemoteUserDataSourceImpl(private val client: HttpClient) : RemoteUserDataSource {

    override suspend fun getUserAbout(userName: String): Result<RemoteUser> {
        return client.get(Endpoint.Users.About(userName))
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> {
        return client.plainRequest<String>(Endpoint.Users.CheckUserName) {
            parameter(Keys.User, userName)
        }.mapCatching { it.toBoolean() }
    }
}