package org.skyfaced.kpm_test.model

import org.skyfaced.kpm_test.utils.extensions.beautifyMoney
import java.text.SimpleDateFormat
import java.util.*

data class ArchiveInfo(
    val id: Int,

    val actualTime: Long,
    val name: String,
    val currencyPair: String,

    val price: Double,
    val minPrice: Double,
    val maxPrice: Double,

    //Unknown
    val cmd: Int,
    val period: String,
    val tradingSystem: Int,
) {
    fun toArchiveItem(): ArchiveItem {
        //FIXME Locale
        val formattedTime = SimpleDateFormat(
            "dd MMMM yyyy, HH:mm:ss",
            Locale("ru", "RU")
        ).format(Date(actualTime * 1000))

        return ArchiveItem(
            id = id,
            actualTime = formattedTime,
            name = name,
            currencyPair = currencyPair,
            price = price.beautifyMoney(""),
            minPrice = minPrice.beautifyMoney(""),
            maxPrice = maxPrice.beautifyMoney("")
        )
    }
}