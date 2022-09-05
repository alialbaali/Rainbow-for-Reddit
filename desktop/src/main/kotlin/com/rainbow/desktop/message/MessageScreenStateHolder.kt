package com.rainbow.desktop.message

import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageScreenStateHolder private constructor(private val initialMessage: Message) {

    private val mutableMessage = MutableStateFlow(initialMessage)
    val message get() = mutableMessage.asStateFlow()

    companion object {
        fun getOrCreateInstance(initialMessage: Message): MessageScreenStateHolder {
            val messageScreenStateHolders = mutableSetOf<MessageScreenStateHolder>()

            return messageScreenStateHolders.find { it.initialMessage == initialMessage }
                ?: MessageScreenStateHolder(initialMessage).also { messageScreenStateHolders += it }
        }
    }
}