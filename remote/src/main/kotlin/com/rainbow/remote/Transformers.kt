package com.rainbow.remote

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

const val NullableEditionDate = 0.0

object EditedTransformingSerializer : JsonTransformingSerializer<Double>(Double.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element.jsonPrimitive.doubleOrNull?.let(::JsonPrimitive) ?: JsonPrimitive(NullableEditionDate)
    }
}