package org.skyfaced.kpm_test.repository.profile

import org.skyfaced.kpm_test.model.ProfileInfo

interface ProfileRepository {
    suspend fun fetchAccountInfo(): ProfileInfo?
}