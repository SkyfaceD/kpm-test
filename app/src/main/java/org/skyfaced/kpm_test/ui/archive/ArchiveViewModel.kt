package org.skyfaced.kpm_test.ui.archive

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.skyfaced.kpm_test.model.ArchiveInfo
import org.skyfaced.kpm_test.model.CurrencyPairItem
import org.skyfaced.kpm_test.repository.archive.ArchiveRepository
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.resultHandler
import org.skyfaced.kpm_test.utils.extensions.resultLoading

class ArchiveViewModel(private val repository: ArchiveRepository) : ViewModel() {
    private val _archiveInfo = MutableSharedFlow<Result<List<ArchiveInfo>>>(1)
    val archiveInfo = _archiveInfo.asSharedFlow()

    /**
     * true -> Expanded
     * false -> Collapsed
     */
    private val _bottomSheetState = MutableStateFlow(false)
    val bottomSheetState = _bottomSheetState.asStateFlow()

    private val _currencyPairs = MutableStateFlow(
        listOf(
            CurrencyPairItem("EURUSD"),
            CurrencyPairItem("GBPUSD"),
            CurrencyPairItem("USDJPY"),
            CurrencyPairItem("USDCHF"),
            CurrencyPairItem("USDCAD"),
            CurrencyPairItem("AUDUSD"),
            CurrencyPairItem("NZDUSD")
        )
    )
    val currencyPairs = _currencyPairs.asStateFlow()

    private val _dates = MutableStateFlow(Pair(-1L, -1L))
    val dates = _dates.asStateFlow()

    fun saveDates(dates: Pair<Long, Long>) {
        _dates.value = dates
    }

    fun toggleBottomSheet(isExpanded: Boolean) {
        _bottomSheetState.value = isExpanded
    }

    fun searchArchiveInfo() {
        viewModelScope.launch {
            _archiveInfo.emit(resultLoading())
            val currencyPairs = currencyPairs.value
                .filter(CurrencyPairItem::state)
                .map(CurrencyPairItem::text)
                .joinToString()
            val from = (dates.value.first / 1000).toInt()
            val to = (dates.value.second / 1000).toInt()
            _archiveInfo.emit(resultHandler(repository.fetchArchiveInfo(currencyPairs, from, to)))
        }
    }

    fun updateCurrencyPairs(position: Int) {
        _currencyPairs.update {
            val mutable = currencyPairs.value.toMutableList()
            val item = mutable.removeAt(position)
            mutable.add(position, item.copy(state = !item.state))
            return@update mutable
        }
    }
}