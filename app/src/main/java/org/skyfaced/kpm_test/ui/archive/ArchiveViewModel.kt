package org.skyfaced.kpm_test.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.skyfaced.kpm_test.model.ArchiveInfo
import org.skyfaced.kpm_test.repository.archive.ArchiveRepository
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.resultHandler
import org.skyfaced.kpm_test.utils.extensions.resultLoading

class ArchiveViewModel(private val repository: ArchiveRepository) : ViewModel() {
    private val _archiveInfo = MutableSharedFlow<Result<List<ArchiveInfo>>>(1)
    val archiveInfo = _archiveInfo.asSharedFlow()

    init {
        val currencyPairs = "EURUSD,GBPUSD,USDJPY,USDCHF,USDCAD,AUDUSD,NZDUSD".split(',')
        val from = 1479860023
        val to = 1480066860
        searchArchiveInfo(currencyPairs, from, to)
    }

    fun searchArchiveInfo(currencyPairs: List<String>, from: Int, to: Int) {
        viewModelScope.launch {
            _archiveInfo.emit(resultLoading())
            _archiveInfo.emit(resultHandler(repository.fetchArchiveInfo(currencyPairs, from, to)))
        }
    }

    fun refresh() {
        val currencyPairs = "EURUSD,GBPUSD,USDJPY,USDCHF,USDCAD,AUDUSD,NZDUSD".split(',')
        val from = 1479860023
        val to = 1480066860
        searchArchiveInfo(currencyPairs, from, to)
    }
}