package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.skyfaced.kpm_test.model.PromotionsInfo

@Serializable
data class CCPromoResponse(
    @SerialName("button_color")
    val buttonColor: String,
    @SerialName("button_text")
    val buttonText: String,
    @SerialName("die_date")
    val dieDate: Int,
    @SerialName("euro_available")
    val euroAvailable: Boolean,
    val image: String,
    val link: String,
    val text: String,
) {
    fun toPromotionsInfo() = PromotionsInfo(
        title = text,
        image = image,
        link = link,
        label = buttonText,
        euroAvailable = euroAvailable,
        dueDate = dieDate
    )
}