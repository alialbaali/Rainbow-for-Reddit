package com.rainbow.desktop.model

import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class ListStateHolder<T : Any> : StateHolder() {

    protected val mutableItems = MutableStateFlow<UIState<List<T>>>(UIState.Loading(emptyList()))
    val items get() = mutableItems.asStateFlow()

    protected val mutableLastItem = MutableStateFlow<T?>(null)
    val lastItem get() = mutableLastItem.asStateFlow()

    abstract val T.itemId: String

    abstract fun loadItems()

    fun setLastItem(lastPost: T?) {
        mutableLastItem.value = lastPost
        loadItems()
    }

    fun updateItem(item: T) {
        mutableItems.value = items.value.map {
            it.map {
                if (it.itemId == item.itemId) item else it
            }
        }
    }
}