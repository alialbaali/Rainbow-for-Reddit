package com.rainbow.common.message

import com.rainbow.common.model.UnSortedListModel
import com.rainbow.domain.models.Message

class MessageListModel(getItems: suspend (String?) -> Result<List<Message>>) : UnSortedListModel<Message>(getItems) {
    override val Message.itemId: String
        get() = this.id
}