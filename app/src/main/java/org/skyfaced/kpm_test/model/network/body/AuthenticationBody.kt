package org.skyfaced.kpm_test.model.network.body

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationBody(
    val login: Int,
    val password: String
)