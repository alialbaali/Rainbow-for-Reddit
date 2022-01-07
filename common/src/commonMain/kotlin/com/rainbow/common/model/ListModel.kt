package com.rainbow.common.model

import com.rainbow.common.utils.UIState
import com.rainbow.common.utils.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class ListModel<T : Any> : Model() {

    protected val mutableItems = MutableStateFlow<UIState<List<T>>>(UIState.Loading)
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