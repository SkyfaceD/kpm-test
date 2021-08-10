package org.skyfaced.kpm_test.repository.profile

import android.content.SharedPreferences
import org.skyfaced.kpm_test.model.AccountType
import org.skyfaced.kpm_test.model.CurrencyCode
import org.skyfaced.kpm_test.model.ProfileInfo
import org.skyfaced.kpm_test.network.PeanutApi
import org.skyfaced.kpm_test.utils.peanutBody
import java.util.*

class ProfileRepositoryImpl(
    private val api: PeanutApi,
    private val preferences: SharedPreferences,
) : ProfileRepository {
    override suspend fun fetchAccountInfo() = try {
        val body = preferences.peanutBody

        val accountInfo = api.accountInformation(body)
        val phoneNumber = api.lastFourNumberPhone(body)

        ProfileInfo(
            username = accountInfo.name,
            phoneNumber = phoneNumber,
            balance = accountInfo.balance,
            equity = accountInfo.equity,
            freeMargin = accountInfo.freeMargin,
            currentTradesCount = accountInfo.currentTradesCount.toString(),
            totalTradesCount = accountInfo.totalTradesCount.toString(),
            currentTradesVolume = accountInfo.currentTradesVolume.toString(),
            totalTradesVolume = accountInfo.totalTradesVolume.toString(),
            countryCode = accountInfo.country.extractCountryCode(),
            city = accountInfo.city,
            address = accountInfo.address,
            zipCode = accountInfo.zipCode,
            currencyCode = CurrencyCode.values()[accountInfo.currency],
            type = AccountType.values()[accountInfo.type],
            verificationLevel = accountInfo.verificationLevel,
            leverage = accountInfo.leverage,
            isAnyOpenTrades = accountInfo.isAnyOpenTrades,
            isSwapFree = accountInfo.isSwapFree,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

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