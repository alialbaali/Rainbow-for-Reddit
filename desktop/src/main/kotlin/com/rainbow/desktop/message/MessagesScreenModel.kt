package com.rainbow.desktop.message

import com.rainbow.desktop.model.Model
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object MessagesScreenModel : Model() {

    private val mutableSelectedTab = MutableStateFlow(MessageTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val inboxMessages = MessageListModel { lastMessageId ->
        Repos.Message.getInboxMessages(lastMessageId)
    }

    val unreadMessages = MessageListModel { lastMessageId ->
        Repos.Message.getUnreadMessages(lastMessageId)
    }

    val sentMessages = MessageListModel { lastMessageId ->
        Repos.Message.getSentMessages(lastMessageId)
    }

    val messages = MessageListModel { lastMessageId ->
        Repos.Message.getMessages(lastMessageId)
    }

    val mentions = MessageListModel { lastMessageId ->
        Repos.Message.getMentions(lastMessageId)
    }

    val postMessages = MessageListModel { lastMessageId ->
        Repos.Message.getPostMessages(lastMessageId)
    }

    val commentMessages = MessageListModel { lastMessageId ->
        Repos.Message.getCommentMessages(lastMessageId)
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    MessageTab.Inbox -> if (inboxMessages.items.value.isLoading) inboxMessages.loadItems()
                    MessageTab.Unread -> if (unreadMessages.items.value.isLoading) unreadMessages.loadItems()
                    MessageTab.Sent -> if (sentMessages.items.value.isLoading) sentMessages.loadItems()
                    MessageTab.Messages -> if (messages.items.value.isLoading) messages.loadItems()
                    MessageTab.Mentions -> if (mentions.items.value.isLoading) mentions.loadItems()
                    MessageTab.PostMessages -> if (postMessages.items.value.isLoading) postMessages.loadItems()
                    MessageTab.CommentMessages -> if (commentMessages.items.value.isLoading) commentMessages.loadItems()
                }
            }
            .launchIn(scope)
    }

    fun selectTab(tab: MessageTab) {
        mutableSelectedTab.value = tab
    }
}