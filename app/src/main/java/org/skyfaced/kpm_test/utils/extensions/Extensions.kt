package org.skyfaced.kpm_test.utils.extensions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun <T> lazySafetyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE) { initializer() }

fun Double.beautifyMoney(symbol: String): SpannableString {
    val currencyFormat = (NumberFormat.getNumberInstance(Locale.US) as DecimalFormat).apply {
        applyPattern(",##0.00 $symbol")

        val symbols = decimalFormatSymbols
        symbols.groupingSeparator = ' '
        decimalFormatSymbols = symbols

        minimumFractionDigits = 2
    }
    val formattedString = currencyFormat.format(this)

    val spannable = SpannableString(formattedString)
    val startPosition = formattedString.indexOf('.')
    spannable.setSpan(
        ForegroundColorSpan(Color.GRAY),
        startPosition,
        formattedString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}