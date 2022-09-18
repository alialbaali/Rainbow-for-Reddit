package com.rainbow.desktop.message

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.domain.models.Message
import com.rainbow.domain.repository.MessageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    private val mutableSelectedItemIds = MutableStateFlow(emptyMap<MessageTab, String>())
    val selectedItemIds get() = mutableSelectedItemIds.asStateFlow()

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

        scope.launch {
            inboxMessages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.Inbox, message.id) }
        }

        scope.launch {
            unreadMessages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.Unread, message.id) }
        }

        scope.launch {
            sentMessages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.Sent, message.id) }
        }

        scope.launch {
            messages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.Messages, message.id) }
        }

        scope.launch {
            mentions.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.Mentions, message.id) }
        }

        scope.launch {
            postMessages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.PostMessages, message.id) }
        }

        scope.launch {
            commentMessages.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { message -> selectItemId(MessageTab.CommentMessages, message.id) }
        }
    }

    companion object {
        val Instance = MessagesScreenStateHolder()
    }

    fun selectTab(tab: MessageTab) {
        mutableSelectedTab.value = tab
    }

    fun selectItemId(tab: MessageTab, id: String) {
        mutableSelectedItemIds.value += tab to id
    }
}