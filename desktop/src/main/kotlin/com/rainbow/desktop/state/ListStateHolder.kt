package com.rainbow.desktop.state

import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import kotlinx.coroutines.flow.*

abstract class ListStateHolder<T : Any>(private val dataItems: Flow<List<T>>) : StateHolder() {

    protected val mutableItems = MutableStateFlow<UIState<List<T>>>(UIState.Empty)
    val items get() = mutableItems.asStateFlow()
    protected val currentItems get() = items.value.getOrNull()

    protected val mutableLastItem = MutableStateFlow<T?>(null)
    val lastItem get() = mutableLastItem.asStateFlow()

    protected abstract suspend fun getItems(lastItem: T?): Result<Unit>

    open fun loadItems() {
        dataItems
            .map { UIState.Success(it) }
            .onEach { mutableItems.value = it }
            .launchIn(scope)

        lastItem
            .onEach { lastItem ->
                mutableItems.value = UIState.Loading(currentItems)
                getItems(lastItem)
                    .onFailure { mutableItems.value = UIState.Failure(currentItems, it) }
            }
            .launchIn(scope)
    }

    fun setLastItem(lastItem: T?) {
        mutableLastItem.value = lastItem
    }

}