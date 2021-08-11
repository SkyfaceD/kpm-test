package org.skyfaced.kpm_test.repository.promotions

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.skyfaced.kpm_test.model.network.response.CCPromoResponse
import org.skyfaced.kpm_test.network.CabinetMicroServiceApi

class PromotionsRepositoryImpl(
    private val api: CabinetMicroServiceApi,
    private val json: Json,
) : PromotionsRepository {
    override suspend fun fetchPromotions(language: String) = try {
        json.decodeFromString<HashMap<String, CCPromoResponse>>(api.getCCPromo(language))
            .values
            .map(CCPromoResponse::toPromotionsInfo)
    } catch (e: Exception) {
        null
    }
}