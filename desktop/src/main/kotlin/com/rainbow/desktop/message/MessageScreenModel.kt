package com.rainbow.desktop.message

import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private val messageScreenModels = mutableSetOf<MessageScreenModel>()

class MessageScreenModel private constructor(private val initialMessage: Message) {

    private val mutableMessage = MutableStateFlow(initialMessage)
    val message get() = mutableMessage.asStateFlow()

    companion object {
        fun getOrCreateInstance(initialMessage: Message): MessageScreenModel {
            return messageScreenModels.find { it.initialMessage == initialMessage }
                ?: MessageScreenModel(initialMessage).also { messageScreenModels += it }
        }
    }
}