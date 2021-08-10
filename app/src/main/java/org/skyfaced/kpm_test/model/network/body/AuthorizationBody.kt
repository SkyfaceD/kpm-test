package org.skyfaced.kpm_test.model.network.body

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationBody(
    val login: Int,
    val token: String,
)