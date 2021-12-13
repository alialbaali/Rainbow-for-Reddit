package com.rainbow.app

import com.rainbow.app.model.ListModel

import com.rainbow.app.model.Model
import com.rainbow.app.model.SortedListModel
import com.rainbow.app.post.PostScreenModel
import com.rainbow.app.utils.Constants
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrNull
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
object RainbowModel : Model() {

    private val mutableListModel = MutableStateFlow<UIState<ListModel<Any>>>(UIState.Loading)

    val sorting
        get() = mutableListModel.map {
            val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
            sortedListModel?.sorting?.value
        }.stateIn(scope, SharingStarted.Lazily, null)

    val timeSorting
        get() = mutableListModel.map {
            val sortedListModel = it.getOrNull() as? SortedListModel<Any, *>
            sortedListModel?.timeSorting?.value
        }.stateIn(scope, SharingStarted.Lazily, null)

    private val mutablePostScreenModel = MutableStateFlow<UIState<PostScreenModel>>(UIState.Loading)
    val postScreenModel get() = mutablePostScreenModel.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
        mutableListModel
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