package com.rainbow.data.utils

import com.rainbow.remote.dto.RemoteComment

val RemoteComment.validPostId: String
    get() = linkId.substringId()

val RemoteComment.validSubId: String
    get() = subredditId.substringId()

val RemoteComment.validUserId: String
    get() = authorFullName.substringId()


private fun String.substringId() = substringAfter('_')

const val DefaultLimit = 25

const val Null = "null"