package com.rainbow.desktop.message

import com.rainbow.desktop.model.UnSortedListStateHolder
import com.rainbow.domain.models.Message

class MessageListStateHolder(getItems: suspend (String?) -> Result<List<Message>>) : UnSortedListStateHolder<Message>(getItems) {
    override val Message.itemId: String
        get() = this.id
}