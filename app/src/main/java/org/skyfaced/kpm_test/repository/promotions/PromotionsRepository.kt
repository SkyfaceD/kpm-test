package org.skyfaced.kpm_test.repository.promotions

import org.skyfaced.kpm_test.model.PromotionsInfo

interface PromotionsRepository {
    suspend fun fetchPromotions(language: String): List<PromotionsInfo>?
}