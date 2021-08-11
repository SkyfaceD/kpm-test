package org.skyfaced.kpm_test.model

data class PromotionsInfo(
    val title: String,
    val image: String,
    val link: String,
    val label: String,
    val euroAvailable: Boolean,
    val dueDate: Int,
)