package com.rainbow.remote.routes.utils

import com.rainbow.remote.Error
import com.rainbow.remote.Item
import com.rainbow.remote.Kind
import io.ktor.client.engine.mock.*
import io.ktor.http.*

internal fun <T> MockRequestHandleScope.respondItem(
    kind: Kind?,
    data: T?,
    status: HttpStatusCode = HttpStatusCode.OK,
) = respondJson(
    Item(kind, data),
    status
)

internal fun MockRequestHandleScope.respondError(
    message: String,
    status: HttpStatusCode = HttpStatusCode.BadRequest,
) = respondJson(
    Error(message, status.value),
    status
)