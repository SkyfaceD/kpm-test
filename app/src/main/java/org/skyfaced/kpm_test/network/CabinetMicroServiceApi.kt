package org.skyfaced.kpm_test.network

interface CabinetMicroServiceApi {
    suspend fun getCCPromo(language: String): String
}