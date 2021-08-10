package org.skyfaced.kpm_test.repository.profile

import android.content.SharedPreferences
import org.skyfaced.kpm_test.network.PeanutApi
import org.skyfaced.kpm_test.utils.extensions.peanutBody

class ProfileRepositoryImpl(
    private val api: PeanutApi,
    private val preferences: SharedPreferences,
) : ProfileRepository {
    override suspend fun fetchAccountInfo() = try {
        val body = preferences.peanutBody

        val phoneNumber = api.lastFourNumberPhone(body)
        val accountInfo = api.accountInformation(body)

        accountInfo.toProfileInfo(phoneNumber)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}