package org.skyfaced.kpm_test.model.network.body

data class AuthorizationBody(
    val login: Int,
    val token: String
)