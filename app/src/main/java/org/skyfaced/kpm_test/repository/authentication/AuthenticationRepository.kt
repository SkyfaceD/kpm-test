package org.skyfaced.kpm_test.repository.authentication

import org.skyfaced.kpm_test.model.network.body.AuthenticationBody

interface AuthenticationRepository {
    suspend fun signIn(body: AuthenticationBody): String?
}