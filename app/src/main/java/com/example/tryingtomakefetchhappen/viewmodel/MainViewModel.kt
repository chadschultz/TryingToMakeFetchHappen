package com.example.tryingtomakefetchhappen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tryingtomakefetchhappen.datastate.DataState
import com.example.tryingtomakefetchhappen.datastate.DataStateTransformer
import com.example.tryingtomakefetchhappen.model.HiringItem
import com.example.tryingtomakefetchhappen.model.HiringItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HiringItemRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val hiringItemTransformer = HiringItemTransformer()

    init {
        viewModelScope.launch {
            repository.hiringItemsResult.collect {result ->
                val hiringItemState = hiringItemTransformer.updateFromResult(
                    _uiState.value.hiringItemState,
                    result
                )

                _uiState.value = _uiState.value.copy(hiringItemState = hiringItemState)
            }
        }
    }

    /**
     * Group [HiringItem] objects by [HiringItem.listId] and sort by [HiringItem.name].
     */
    class HiringItemTransformer: DataStateTransformer<Map<Int, List<HiringItem>>, List<HiringItem>>() {
        override fun transformData(sourceData: List<HiringItem>): Map<Int, List<HiringItem>> {
            return sourceData.filter { !it.name.isNullOrBlank() }
                .groupBy { it.listId }
                .mapValues { (_, items) ->
                    // 'Sort the results first by "listId" then by "name" when displaying.'
                    // This requirement is likely an error. Sorting by a String means that "28"
                    // comes before "280." In a real world situation, this is where I'd talk to
                    // the PM and ask if I could sort by the ID instead.
                    items.sortedBy { it.name }
                }.toSortedMap()
        }
    }

    data class UiState(val hiringItemState: DataState<Map<Int, List<HiringItem>>> = DataState())
}