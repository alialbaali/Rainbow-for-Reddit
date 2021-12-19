package com.rainbow.app

import com.rainbow.app.message.MessageScreenModel
import com.rainbow.app.model.ListModel
import com.rainbow.app.model.Model
import com.rainbow.app.model.SortedListModel
import com.rainbow.app.post.PostScreenModel
import com.rainbow.app.utils.Constants
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrNull
import com.rainbow.domain.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
object RainbowModel : Model() {

    private val mutableListModel = MutableStateFlow<UIState<ListModel<Any>>>(UIState.Loading)

    private val items = callbackFlow {
        mutableListModel
            .onEach { it.getOrNull()?.items?.collect { send(it) } }
            .launchIn(scope)
        awaitClose()
    }.stateIn(scope, SharingStarted.Lazily, UIState.Loading)

    val sorting = callbackFlow {
        mutableListModel
            .onEach {
                val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
                sortedListModel?.sorting?.collect { send(it) }
            }
            .launchIn(scope)
        awaitClose()
    }.stateIn(scope, SharingStarted.Lazily, null)

    val timeSorting = callbackFlow {
        mutableListModel
            .onEach {
                val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
                sortedListModel?.timeSorting?.collect { send(it) }
            }.launchIn(scope)
        awaitClose()
    }.stateIn(scope, SharingStarted.Lazily, null)

    private val mutablePostScreenModel = MutableStateFlow<UIState<PostScreenModel>>(UIState.Loading)
    val postScreenModel get() = mutablePostScreenModel.asStateFlow()

    private val mutableMessageScreenModel = MutableStateFlow<UIState<MessageScreenModel>>(UIState.Loading)
    val messageScreenModel get() = mutableMessageScreenModel.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
        combine(items, sorting, timeSorting) { items, _, _ ->
            items.getOrNull()
                ?.firstOrNull()
                ?.also { item ->
                    val postType = when (item) {
                        is Post -> PostScreenModel.Type.PostEntity(item)
                        is Comment -> PostScreenModel.Type.PostId(item.postId)
                        else -> null
                    }
                    if (postType != null)
                        selectPost(postType)
                    else if (item is Message)
                        selectMessageOrPost(item)
                }
        }.launchIn(scope)

        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach { mutableListModel.value.getOrNull()?.loadItems() }
            .launchIn(scope)
    }

    fun setListModel(model: ListModel<*>) {
        mutableListModel.value = UIState.Success(model as ListModel<Any>)
    }

    fun selectPost(type: PostScreenModel.Type) {
        val model = PostScreenModel.getOrCreateInstance(type)
        mutablePostScreenModel.value = UIState.Success(model)
    }

    fun selectMessageOrPost(message: Message) {
        val postId = when (message.type) {
            is Message.Type.PostReply -> (message.type as Message.Type.PostReply).postId
            is Message.Type.CommentReply -> (message.type as Message.Type.CommentReply).postId
            is Message.Type.Mention -> (message.type as Message.Type.Mention).postId
            else -> null
        }
        if (postId != null)
            selectPost(PostScreenModel.Type.PostId(postId))
        val model = MessageScreenModel.getOrCreateInstance(message)
        mutableMessageScreenModel.value = UIState.Success(model)
    }

    fun updatePost(post: Post) {
        mutableListModel.value.getOrNull()?.updateItem(post)
        postScreenModel.value.getOrNull()?.updatePost(post)
    }

    fun updateComment(comment: Comment) {
        val isCommentListModel = mutableListModel.value.getOrNull()
            ?.items?.value
            ?.getOrNull()
            ?.any { it::class == Comment::class } ?: false
        if (isCommentListModel)
            mutableListModel.value.getOrNull()?.updateItem(comment)
        postScreenModel.value.getOrNull()?.commentListModel?.value?.updateComment(comment)
    }

    fun setSorting(sorting: Sorting) {
        val sortedListModel = mutableListModel.value.getOrNull() as? SortedListModel<Any, Sorting>
        sortedListModel?.setSorting(sorting)
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        val sortedListModel = mutableListModel.value.getOrNull() as? SortedListModel<Any, Sorting>
        sortedListModel?.setTimeSorting(timeSorting)
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }
}