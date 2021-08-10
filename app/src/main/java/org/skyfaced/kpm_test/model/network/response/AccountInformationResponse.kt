package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.Serializable
import org.skyfaced.kpm_test.model.AccountType
import org.skyfaced.kpm_test.model.CurrencyCode

import org.skyfaced.kpm_test.model.ExtensionData
import org.skyfaced.kpm_test.model.ProfileInfo
import java.util.*

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
    val zipCode: String,
) {
    fun toProfileInfo(phoneNumber: String) = ProfileInfo(
        username = name,
        phoneNumber = phoneNumber,
        balance = balance,
        equity = equity,
        freeMargin = freeMargin,
        currentTradesCount = currentTradesCount.toString(),
        totalTradesCount = totalTradesCount.toString(),
        currentTradesVolume = currentTradesVolume.toString(),
        totalTradesVolume = totalTradesVolume.toString(),
        countryCode = country.extractCountryCode(),
        city = city,
        address = address,
        zipCode = zipCode,
        currencyCode = CurrencyCode.values()[currency],
        type = AccountType.values()[type],
        verificationLevel = verificationLevel,
        leverage = leverage,
        isAnyOpenTrades = isAnyOpenTrades,
        isSwapFree = isSwapFree,
    )

    private fun String.extractCountryCode(lowerCase: Boolean = true): String {
        val countries = Locale.getISOCountries()
        for (country in countries) {
            val locale = Locale("", country)
            if (locale.displayName == this) {
                return if (lowerCase) locale.country.lowercase() else locale.country
            }
        }
        return ""
    }
}