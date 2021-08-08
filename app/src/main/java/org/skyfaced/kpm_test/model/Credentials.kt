package org.skyfaced.kpm_test.model

data class Credentials(
    val login: Int,
    val password: String,
    val remember: Boolean,
    val peanutToken: String,
    val partnerToken: String
)