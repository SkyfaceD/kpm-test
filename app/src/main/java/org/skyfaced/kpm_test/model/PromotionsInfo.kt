package org.skyfaced.kpm_test.model

import org.skyfaced.kpm_test.model.adapter.PromotionsItem

data class PromotionsInfo(
    val title: String,
    val image: String,
    val link: String,
    val label: String,
    val euroAvailable: Boolean,
    val dueDate: Int,
) {
    fun toPromotionItem() = PromotionsItem(
        title = title,
        image = image,
        link = link,
        label = label
    )
}