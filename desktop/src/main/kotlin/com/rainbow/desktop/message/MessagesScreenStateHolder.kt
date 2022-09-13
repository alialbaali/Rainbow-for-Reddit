package com.rainbow.desktop.message

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.domain.models.Message
import com.rainbow.domain.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MessagesScreenStateHolder(
    private val messageRepository: MessageRepository = Repos.Message,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(MessageTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val inboxMessages = object : MessagesStateHolder(messageRepository.inboxMessages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getInboxMessages(lastItem?.id)
        }
    }

    val unreadMessages = object : MessagesStateHolder(messageRepository.unreadMessages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getUnreadMessages(lastItem?.id)
        }
    }

    val sentMessages = object : MessagesStateHolder(messageRepository.sentMessages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getSentMessages(lastItem?.id)
        }
    }

    val messages = object : MessagesStateHolder(messageRepository.messages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getMessages(lastItem?.id)
        }
    }

    val mentions = object : MessagesStateHolder(messageRepository.mentions) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getMentions(lastItem?.id)
        }
    }

    val postMessages = object : MessagesStateHolder(messageRepository.postMessages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getPostMessages(lastItem?.id)
        }
    }

    val commentMessages = object : MessagesStateHolder(messageRepository.commentMessages) {
        override suspend fun getItems(lastItem: Message?): Result<Unit> {
            return messageRepository.getCommentMessages(lastItem?.id)
        }
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    MessageTab.Inbox -> if (inboxMessages.items.value.isEmpty) inboxMessages.loadItems()
                    MessageTab.Unread -> if (unreadMessages.items.value.isEmpty) unreadMessages.loadItems()
                    MessageTab.Sent -> if (sentMessages.items.value.isEmpty) sentMessages.loadItems()
                    MessageTab.Messages -> if (messages.items.value.isEmpty) messages.loadItems()
                    MessageTab.Mentions -> if (mentions.items.value.isEmpty) mentions.loadItems()
                    MessageTab.PostMessages -> if (postMessages.items.value.isEmpty) postMessages.loadItems()
                    MessageTab.CommentMessages -> if (commentMessages.items.value.isEmpty) commentMessages.loadItems()
                }
            }
            .launchIn(scope)
    }

    companion object {
        val Instance = MessagesScreenStateHolder()
    }

    fun selectTab(tab: MessageTab) {
        mutableSelectedTab.value = tab
    }
}