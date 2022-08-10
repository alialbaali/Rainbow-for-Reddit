package com.rainbow.desktop.model

import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.desktop.utils.toUIState
import kotlinx.coroutines.launch

abstract class UnSortedListStateHolder<T : Any>(private val getItems: suspend (String?) -> Result<List<T>>) : ListStateHolder<T>() {
    final override fun loadItems() {
        scope.launch {
            if (lastItem.value == null) mutableItems.value = UIState.Loading
            mutableItems.value = getItems(lastItem.value?.itemId)
                .map {
                    if (lastItem.value != null)
                        mutableItems.value.getOrDefault(emptyList()) + it
                    else
                        it
                }
                .toUIState()
        }
    }
}