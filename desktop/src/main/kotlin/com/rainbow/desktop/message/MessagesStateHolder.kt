package com.rainbow.desktop.message

import com.rainbow.desktop.state.ListStateHolder
import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.Flow

abstract class MessagesStateHolder(items: Flow<List<Message>>) : ListStateHolder<Message>(items)