package com.rainbow.desktop.model

import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import kotlinx.coroutines.flow.*

abstract class ListStateHolder<T : Any> : StateHolder() {

    protected val mutableItems = MutableStateFlow<UIState<List<T>>>(UIState.Loading(emptyList()))
    val items get() = mutableItems.asStateFlow()
    private val currentItems get() = items.value.getOrNull()

    protected val mutableLastItem = MutableStateFlow<T?>(null)
    val lastItem get() = mutableLastItem.asStateFlow()

    abstract suspend fun loadItems(lastItem: T?): Result<Unit>

    init {
        lastItem
            .filterNotNull()
            .onEach { lastItem ->
                mutableItems.value = UIState.Loading(currentItems)
                loadItems(lastItem)
                    .onFailure { mutableItems.value = UIState.Failure(currentItems, it) }
            }
            .launchIn(scope)
    }

    fun setLastItem(lastItem: T?) {
        mutableLastItem.value = lastItem
    }

}