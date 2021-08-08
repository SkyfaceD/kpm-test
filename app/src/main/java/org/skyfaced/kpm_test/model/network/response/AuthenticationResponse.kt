package org.skyfaced.kpm_test.model.network.response

import kotlinx.serialization.Serializable
import org.skyfaced.kpm_test.model.ExtensionData

@Serializable
data class AuthenticationResponse(
    val extensionData: ExtensionData,
    val result: Boolean,
    val token: String
)

