package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteWikiPage

interface RemoteWikiDataSource {

    suspend fun getWikiIndex(subredditName: String): Result<RemoteWikiPage>

    suspend fun getWikiPage(subredditName: String, pageName: String): Result<RemoteWikiPage>

}