package org.skyfaced.kpm_test.repository.authentication

import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.network.PeanutApi
import timber.log.Timber

class PeanutAuthenticationRepositoryImpl(
    private val peanutApi: PeanutApi
) : AuthenticationRepository {
    override suspend fun signIn(body: AuthenticationBody) = try {
        peanutApi.signIn(body).token
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}