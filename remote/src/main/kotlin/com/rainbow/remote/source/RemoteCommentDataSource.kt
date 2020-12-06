package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteComment

interface RemoteCommentDataSource {

    suspend fun getComments(postIdPrefixed: String): RedditResponse<List<RemoteComment>>

    suspend fun saveComment(commentIdPrefixed: String): RedditResponse<Unit>

    suspend fun unSaveComment(commentIdPrefixed: String): RedditResponse<Unit>

}