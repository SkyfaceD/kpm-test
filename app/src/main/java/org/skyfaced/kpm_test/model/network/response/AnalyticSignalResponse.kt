package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.Serializable

@Serializable
data class AnalyticSignalResponse(
    val actualTime: Int,
    val cmd: Int,
    val comment: String,
    val id: Int,
    val pair: String,
    val period: String,
    val price: Double,
    val sl: Double,
    val tp: Double,
    val tradingSystem: Int
)