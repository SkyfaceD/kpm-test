package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.skyfaced.kpm_test.model.ArchiveInfo

@Serializable
data class AnalyticSignalsResponse(
    @SerialName("ActualTime")
    val actualTime: Long,
    @SerialName("Cmd")
    val cmd: Int,
    @SerialName("Comment")
    val comment: String,
    @SerialName("Id")
    val id: Int,
    @SerialName("Pair")
    val pair: String,
    @SerialName("Period")
    val period: String,
    @SerialName("Price")
    val price: Double,
    @SerialName("Sl")
    val sl: Double,
    @SerialName("Tp")
    val tp: Double,
    @SerialName("TradingSystem")
    val tradingSystem: Int,
) {
    fun toArchiveInfo() = ArchiveInfo(
        id = id,
        actualTime = actualTime,
        name = comment,
        currencyPair = pair,
        price = price,
        minPrice = sl,
        maxPrice = tp,
        cmd = cmd,
        period = period,
        tradingSystem = tradingSystem
    )
}