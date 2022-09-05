package com.rainbow.desktop.message

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.repository.MessageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MessageScreenStateHolder private constructor(
    private val messageId: String,
    private val messageRepository: MessageRepository = Repos.Message,
) : StateHolder() {

    val message = messageRepository.getMessage(messageId)
        .map { it.toUIState() }
        .stateIn(scope, SharingStarted.Eagerly, UIState.Loading())

    companion object {
        fun getInstance(messageId: String): MessageScreenStateHolder {
            val messageScreenStateHolders = mutableSetOf<MessageScreenStateHolder>()

            return messageScreenStateHolders.find { it.messageId == messageId }
                ?: MessageScreenStateHolder(messageId).also { messageScreenStateHolders += it }
        }
    }
}