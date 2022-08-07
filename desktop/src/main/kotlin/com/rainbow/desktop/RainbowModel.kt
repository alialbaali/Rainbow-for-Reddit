package com.rainbow.desktop

import com.rainbow.desktop.message.MessageScreenModel
import com.rainbow.desktop.post.PostScreenModel
import com.rainbow.desktop.subreddit.SubredditScreenModel
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.model.Model
import com.rainbow.desktop.model.SortedListModel
import com.rainbow.desktop.utils.Constants
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.domain.models.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
object RainbowModel : Model() {

    private val mutableListModel = MutableSharedFlow<ListModel<Any>>(replay = Int.MAX_VALUE)

    private val items = mutableListModel
        .map { it.items.value }
        .stateIn(scope, SharingStarted.Lazily, UIState.Loading)

    val sorting = mutableListModel
        .map { it as? SortedListModel<Any, *> }
        .map { it?.sorting?.value }
        .stateIn(scope, SharingStarted.Lazily, null)

    val timeSorting = mutableListModel
        .map { it as? SortedListModel<Any, *> }
        .map { it?.timeSorting?.value }
        .stateIn(scope, SharingStarted.Lazily, null)

    private val mutablePostScreenModelType = MutableStateFlow<UIState<PostScreenModel.Type>>(UIState.Loading)
    val postScreenModelType get() = mutablePostScreenModelType.asStateFlow()

    private val mutableMessageScreenModel = MutableStateFlow<UIState<MessageScreenModel>>(UIState.Loading)
    val messageScreenModel get() = mutableMessageScreenModel.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
        combine(items, sorting, timeSorting) { items, _, _ ->
            items.getOrNull()
                ?.firstOrNull()
                ?.also { item ->
                    when (item) {
                        is Post -> PostScreenModel.Type.PostEntity(item).also(this::selectPost)
                        is Comment -> PostScreenModel.Type.PostId(item.postId).also(this::selectPost)
                        is Message -> selectMessageOrPost(item)
                    }
                }
        }.launchIn(scope)

        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach { mutableListModel.replayCache.last().loadItems() }
            .launchIn(scope)
    }

    fun setListModel(model: ListModel<*>) {
        scope.launch {
            mutableListModel.emit(model as ListModel<Any>)
        }
    }

    fun selectPost(type: PostScreenModel.Type) {
        mutablePostScreenModelType.value = UIState.Success(type)
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
        mutableListModel.replayCache
            .filter { it.checkType<Post>() }
            .onEach { it.updateItem(post) }
        PostScreenModel.getOrCreateInstance(PostScreenModel.Type.PostEntity(post)).updatePost(post)
    }

    fun updateComment(comment: Comment) {
        mutableListModel.replayCache
            .filter { it.checkType<Comment>() }
            .onEach { it.updateItem(comment) }
        PostScreenModel.getOrCreateInstance(PostScreenModel.Type.PostId(comment.postId))
            .commentListModel
            .value
            .updateComment(comment)
    }

    fun updateSubreddit(subreddit: Subreddit) {
        mutableListModel.replayCache
            .filter { it.checkType<Subreddit>() }
            .onEach { it.updateItem(subreddit) }
        SubredditScreenModel.updateSubreddit(subreddit)
    }

    fun setSorting(sorting: Sorting) {
        scope.launch {
            val sortedListModel = mutableListModel.replayCache.last() as? SortedListModel<Any, Sorting>
            sortedListModel?.setSorting(sorting)
        }
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        val sortedListModel = mutableListModel.replayCache.last() as? SortedListModel<Any, Sorting>
        sortedListModel?.setTimeSorting(timeSorting)
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }

    private inline fun <reified T> ListModel<Any>.checkType(): Boolean {
        return items.value.getOrDefault(emptyList()).any { it::class == T::class }
    }
}