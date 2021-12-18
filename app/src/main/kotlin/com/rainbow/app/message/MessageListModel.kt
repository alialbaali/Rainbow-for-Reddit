package com.rainbow.app.message

import com.rainbow.app.model.UnSortedListModel
import com.rainbow.domain.models.Message

class MessageListModel(getItems: suspend (String?) -> Result<List<Message>>) : UnSortedListModel<Message>(getItems) {
    override val Message.itemId: String
        get() = this.id
}