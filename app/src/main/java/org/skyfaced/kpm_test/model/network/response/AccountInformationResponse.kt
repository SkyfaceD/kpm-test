package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.Serializable

import org.skyfaced.kpm_test.model.ExtensionData

@Serializable
data class AccountInformationResponse(
    val address: String,
    val balance: Double,
    val city: String,
    val country: String,
    val currency: Int,
    val currentTradesCount: Int,
    val currentTradesVolume: Int,
    val equity: Double,
    val extensionData: ExtensionData,
    val freeMargin: Double,
    val isAnyOpenTrades: Boolean,
    val isSwapFree: Boolean,
    val leverage: Int,
    val name: String,
    val phone: String,
    val totalTradesCount: Int,
    val totalTradesVolume: Int,
    val type: Int,
    val verificationLevel: Int,
    val zipCode: String
)