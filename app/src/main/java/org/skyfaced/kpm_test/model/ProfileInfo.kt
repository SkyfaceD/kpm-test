package org.skyfaced.kpm_test.model

/**
 * @param countryCode contains empty string or code in ISO 3166
 * @see [https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes](ISO 3166)
 */
data class ProfileInfo(
    val username: String,
    val phoneNumber: String,

    val balance: Double,
    val equity: Double,
    val freeMargin: Double,

    val currentTradesCount: String,
    val totalTradesCount: String,

    val currentTradesVolume: String,
    val totalTradesVolume: String,

    val countryCode: String,
    val city: String,
    val address: String,
    val zipCode: String,

    //Unknown
    val currencyCode: CurrencyCode,
    val type: AccountType,
    val verificationLevel: Int,
    val leverage: Int,
    val isAnyOpenTrades: Boolean,
    val isSwapFree: Boolean,
) {
    /**
     * @see [https://en.wikipedia.org/wiki/ISO_4217](ISO 4217)
     */
    enum class CurrencyCode(val symbol: String) {
        RUB("₽"),

        //Unknown
        A(""),
        B(""),
        C(""),
        D("")
    }

    //Unknown
    enum class AccountType {
        A,
        B,
        C,
        D,
        E,
        F
    }
}