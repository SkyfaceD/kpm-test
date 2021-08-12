package org.skyfaced.kpm_test.repository.archive

import android.content.SharedPreferences
import org.skyfaced.kpm_test.model.network.response.AnalyticSignalsResponse
import org.skyfaced.kpm_test.network.PartnerApi
import org.skyfaced.kpm_test.utils.extensions.partnerBody
import timber.log.Timber

class ArchiveRepositoryImpl(
    private val api: PartnerApi,
    private val preferences: SharedPreferences,
) : ArchiveRepository {
    private val partnerBody get() = preferences.partnerBody

    override suspend fun fetchArchiveInfo(currencyPairs: String, from: Int, to: Int) = try {
        api.analyticSignals(partnerBody.login, partnerBody.token, currencyPairs, from, to)
            .map(AnalyticSignalsResponse::toArchiveInfo)
    } catch (e: Exception) {
        Timber.e(e.cause)
        null
    }
}