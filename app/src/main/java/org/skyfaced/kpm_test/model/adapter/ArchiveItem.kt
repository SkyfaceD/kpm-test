package org.skyfaced.kpm_test.model.adapter

data class ArchiveItem(
    val id: Int,
    val actualTime: String,
    val name: String,
    val currencyPair: String,
    val price: CharSequence,
    val minPrice: CharSequence,
    val maxPrice: CharSequence,
)
