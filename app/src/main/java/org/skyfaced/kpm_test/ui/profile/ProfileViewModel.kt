package org.skyfaced.kpm_test.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.skyfaced.kpm_test.model.ProfileInfo
import org.skyfaced.kpm_test.repository.profile.ProfileRepository
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.resultHandler
import org.skyfaced.kpm_test.utils.resultLoading

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _profileInfo = MutableSharedFlow<Result<ProfileInfo>>(1)
    val profileInfo = _profileInfo.asSharedFlow()

    init {
        fetchProfileInfo()
    }

    fun fetchProfileInfo() {
        viewModelScope.launch {
            _profileInfo.emit(resultLoading())
            _profileInfo.emit(resultHandler(repository.fetchAccountInfo()))
        }
    }
}