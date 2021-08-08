package org.skyfaced.kpm_test.ui.authentication

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.skyfaced.kpm_test.model.Credentials
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.repository.PartnerAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.repository.PeanutAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.utils.*
import timber.log.Timber

class AuthenticationViewModel(
    private val peanutAuthRepo: PeanutAuthenticationRepositoryImpl,
    private val partnerAuthRepo: PartnerAuthenticationRepositoryImpl,
    private val preferences: SharedPreferences
) : ViewModel() {
    private val _signIn = MutableSharedFlow<Result<Pair<String, String>>>(1)
    val signIn = _signIn.asSharedFlow()

    private val _saveCredentialsNotification = MutableStateFlow(false)
    val saveCredentialsNotification = _saveCredentialsNotification.asStateFlow()

    fun signIn(body: AuthenticationBody) {
        Timber.d("Sign In Started")
        viewModelScope.launch {
            _signIn.emit(resultLoading())
            val token1 = peanutAuthRepo.signIn(body)
            val token2 = partnerAuthRepo.signIn(body)
            _signIn.emit(
                if (listOf(token1, token2).all { !it.isNullOrEmpty() }) {
                    resultSuccess(token1!! to token2!!)
                } else {
                    resultError()
                }
            )
        }
    }

    fun saveCredentials(credentials: Credentials) {
        Timber.d("Save Credentials Started")
        viewModelScope.launch {
            preferences.edit(commit = true) {
                putInt(PREFERENCES_LOGIN, credentials.login)
                if (credentials.remember) {
                    putString(PREFERENCES_PASSWORD, credentials.password)
                }
                putBoolean(PREFERENCES_REMEMBER, credentials.remember)
                putString(PREFERENCES_PEANUT_TOKEN, credentials.peanutToken)
                putString(PREFERENCES_PARTNER_TOKEN, credentials.partnerToken)
            }

            _saveCredentialsNotification.emit(true)
        }
    }
}