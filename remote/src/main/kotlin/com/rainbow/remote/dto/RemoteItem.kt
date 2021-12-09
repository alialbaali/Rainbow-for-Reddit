package com.rainbow.remote.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

private const val RemoteItemDiscriminator = "body"

@Serializable(RemoteItemSerializer::class)
sealed class RemoteItem

private object RemoteItemSerializer : JsonContentPolymorphicSerializer<RemoteItem>(RemoteItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out RemoteItem> {
        return when (RemoteItemDiscriminator) {
            in element.jsonObject -> RemoteComment.serializer()
            else -> RemotePost.serializer()
        }
    }
}