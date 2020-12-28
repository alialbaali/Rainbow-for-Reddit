package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteComment

interface RemoteCommentDataSource {

    suspend fun getComments(postIdPrefixed: String): Result<List<RemoteComment>>

    suspend fun saveComment(commentIdPrefixed: String): Result<Unit>

    suspend fun unSaveComment(commentIdPrefixed: String): Result<Unit>

}