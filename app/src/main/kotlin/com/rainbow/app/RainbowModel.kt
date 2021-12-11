package com.rainbow.app

import com.rainbow.app.model.ListModel

import com.rainbow.app.model.Model
import com.rainbow.app.model.SortedListModel
import com.rainbow.app.post.PostScreenModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrNull
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

private const val DebounceTime = 500L

@OptIn(FlowPreview::class)
object RainbowModel : Model() {

    private val mutableListModel = MutableStateFlow<UIState<ListModel<Any>>>(UIState.Loading)
    val listModel get() = mutableListModel.asStateFlow()

    val sorting
        get() = listModel.map {
            val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
            sortedListModel?.sorting?.value
        }.stateIn(scope, SharingStarted.Lazily, null)

    val timeSorting
        get() = listModel.map {
            val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
            sortedListModel?.timeSorting?.value
        }.stateIn(scope, SharingStarted.Lazily, null)

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
        listModel
            .onEach {
                it.getOrNull()?.items
                    ?.firstOrNull { it.isSuccess }
                    ?.getOrNull()
                    ?.firstOrNull()
                    ?.let { item ->
                        when (item) {
                            is Post -> PostScreenModel.Type.PostEntity(item)
                            is Comment -> PostScreenModel.Type.PostId(item.postId)
                            else -> null
                        }
                    }
                    ?.let(this::selectPost)
            }
            .launchIn(scope)

        mutableRefreshContent
            .debounce(DebounceTime)
            .onEach { listModel.value.getOrNull()?.loadItems() }
            .launchIn(scope)
    }

    private val mutablePostScreenModel = MutableStateFlow<UIState<PostScreenModel>>(UIState.Loading)
    val postScreenModel get() = mutablePostScreenModel.asStateFlow()

    fun setListModel(model: ListModel<*>) {
        mutableListModel.value = UIState.Success(model as ListModel<Any>)
    }

    fun selectPost(type: PostScreenModel.Type) {
        val model = PostScreenModel.getOrCreateInstance(type)
        mutablePostScreenModel.value = UIState.Success(model)
    }

    fun updatePost(post: Post) {
        listModel.value.getOrNull()?.updateItem(post)
        postScreenModel.value.getOrNull()?.updatePost(post)
    }

    fun updateComment(comment: Comment) {
        val isCommentListModel = listModel.value.getOrNull()
            ?.items?.value
            ?.getOrNull()
            ?.any { it::class == Comment::class } ?: false
        if (isCommentListModel)
            listModel.value.getOrNull()?.updateItem(comment)
        postScreenModel.value.getOrNull()?.commentListModel?.updateComment(comment)
    }

    fun setSorting(sorting: Sorting) {
        val sortedListModel = listModel.value.getOrNull() as? SortedListModel<Any, Sorting>
        sortedListModel?.setSorting(sorting)
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        val sortedListModel = listModel.value.getOrNull() as? SortedListModel<Any, Sorting>
        sortedListModel?.setTimeSorting(timeSorting)
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }
}