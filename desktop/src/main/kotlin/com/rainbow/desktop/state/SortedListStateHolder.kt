package com.rainbow.desktop.state

import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

abstract class SortedListStateHolder<T : Any, S : Sorting>(initialSorting: S, private val dataItems: Flow<List<T>>) :
    ListStateHolder<T>(dataItems) {

    private val mutableSorting = MutableStateFlow(initialSorting)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private var previousJob: Job? = null

    final override suspend fun getItems(lastItem: T?): Result<Unit> {
        return getItems(sorting.value, timeSorting.value, lastItem)
    }

    protected abstract suspend fun getItems(sorting: S, timeSorting: TimeSorting, lastItem: T?): Result<Unit>

    final override fun loadItems() {
        lastItem
            .filterNotNull()
            .onEach { lastItem ->
                mutableItems.value = UIState.Loading(currentItems)
                getItems(lastItem)
                    .onFailure { mutableItems.value = UIState.Failure(currentItems, it) }
            }
            .launchIn(scope)

        combine(
            sorting,
            timeSorting,
        ) { sorting, timeSorting ->
            previousJob?.cancel()
            mutableLastItem.value = null
            mutableItems.value = UIState.Loading()
            getItems(sorting, timeSorting, lastItem = null)
                .onSuccess {
                    previousJob = dataItems
                        .map { UIState.Success(it) }
                        .onEach { mutableItems.value = it }
                        .launchIn(scope)
                }
                .onFailure { mutableItems.value = UIState.Failure(null, it) }
        }.launchIn(scope)
    }

    fun setSorting(sorting: S) {
        mutableSorting.value = sorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

}