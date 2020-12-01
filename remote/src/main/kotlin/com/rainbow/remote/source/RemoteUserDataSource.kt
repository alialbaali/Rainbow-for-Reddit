package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteUser

interface RemoteUserDataSource {

    suspend fun getUserAbout(userName: String): RedditResponse<RemoteUser>

    suspend fun checkUserName(userName: String): RedditResponse<Boolean>

}