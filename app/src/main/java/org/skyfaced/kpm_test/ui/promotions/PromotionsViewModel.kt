package org.skyfaced.kpm_test.ui.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.skyfaced.kpm_test.model.PromotionsInfo
import org.skyfaced.kpm_test.model.adapter.PromotionsItem
import org.skyfaced.kpm_test.repository.promotions.PromotionsRepository
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.resultHandler
import org.skyfaced.kpm_test.utils.extensions.resultLoading

class PromotionsViewModel(
    private val repository: PromotionsRepository,
) : ViewModel() {
    private val _promotionsInfo = MutableSharedFlow<Result<List<PromotionsItem>>>(1)
    val promotionsInfo = _promotionsInfo.asSharedFlow()

    init {
        getPromotionsInfo()
    }

    fun getPromotionsInfo(language: String = "en") {
        viewModelScope.launch {
            _promotionsInfo.emit(resultLoading())
            _promotionsInfo.emit(
                resultHandler(
                    repository.fetchPromotions(language)
                        ?.map(PromotionsInfo::toPromotionItem)
                )
            )
        }
    }
}