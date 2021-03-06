package org.skyfaced.kpm_test.repository.authentication

import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.network.PartnerApi
import timber.log.Timber

class PartnerAuthenticationRepositoryImpl(
    private val partnerApi: PartnerApi,
) {
    suspend fun signIn(body: AuthenticationBody) = try {
        partnerApi.signIn(body)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}