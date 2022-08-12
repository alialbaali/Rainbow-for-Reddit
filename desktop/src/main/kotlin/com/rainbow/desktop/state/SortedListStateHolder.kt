package com.rainbow.desktop.state

import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Sorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

abstract class SortedListStateHolder<T : Any, S : Sorting>(initialSorting: S, items: Flow<List<T>>) :
    ListStateHolder<T>() {

    private val mutableSorting = MutableStateFlow(initialSorting)
    val sorting get() = mutableSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private var previousJob: Job? = null

    init {
        combine(
            sorting,
            timeSorting,
        ) { sorting, timeSorting ->
            previousJob?.cancel()
            mutableLastItem.value = null
            mutableItems.value = UIState.Loading()
            loadItems(sorting, timeSorting, lastItem = null)
                .onSuccess {
                    previousJob = items
                        .map { UIState.Success(it) }
                        .onEach { mutableItems.value = it }
                        .launchIn(scope)
                }
                .onFailure { mutableItems.value = UIState.Failure(null, it) }
        }.launchIn(scope)
    }

    final override suspend fun loadItems(lastItem: T?): Result<Unit> {
        return loadItems(sorting.value, timeSorting.value, lastItem)
    }

    protected abstract suspend fun loadItems(sorting: S, timeSorting: TimeSorting, lastItem: T?): Result<Unit>

    fun setSorting(sorting: S) {
        mutableSorting.value = sorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

}