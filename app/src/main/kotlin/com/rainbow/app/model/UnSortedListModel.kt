package com.rainbow.app.model

import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrDefault
import com.rainbow.app.utils.toUIState
import kotlinx.coroutines.launch

abstract class UnSortedListModel<T : Any>(private val getItems: suspend (String?) -> Result<List<T>>) : ListModel<T>() {
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