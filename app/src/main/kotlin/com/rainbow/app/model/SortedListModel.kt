package com.rainbow.app.model

import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrDefault
import com.rainbow.app.utils.toUIState
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class SortedListModel<T : Any, S : Sorting>(
    val initialPostSorting: S,
    private val getItems: suspend (S, TimeSorting, String?) -> Result<List<T>>,
) : ListModel<T>() {

    private val mutablePostSorting = MutableStateFlow(initialPostSorting)
    val postSorting get() = mutablePostSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    final override fun loadItems() {
        scope.launch {
            if (lastItem.value == null) mutableItems.value = UIState.Loading
            mutableItems.value = getItems(postSorting.value, timeSorting.value, lastItem.value?.itemId)
                .map {
                    if (lastItem.value != null)
                        mutableItems.value.getOrDefault(emptyList()) + it
                    else
                        it
                }
                .toUIState()
        }
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
        mutableLastItem.value = null
        loadItems()
    }

    fun setPostSorting(postSorting: S) {
        mutablePostSorting.value = postSorting
        mutableLastItem.value = null
        loadItems()
    }
}