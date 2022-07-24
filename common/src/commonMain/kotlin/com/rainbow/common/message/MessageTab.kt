package com.rainbow.common.message

enum class MessageTab {
    Inbox, Unread, Sent, Messages, Mentions, PostMessages, CommentMessages;

    companion object {
        val Default = Inbox
    }
}