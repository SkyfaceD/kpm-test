package org.skyfaced.kpm_test.utils.extensions

import android.annotation.SuppressLint
import android.content.SharedPreferences
import org.skyfaced.kpm_test.model.Credentials
import org.skyfaced.kpm_test.model.network.body.AuthorizationBody
import org.skyfaced.kpm_test.utils.*

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clear(commit: Boolean = false) {
    if (commit) {
        edit().clear().commit()
    } else {
        edit().clear().apply()
    }
}

val SharedPreferences.login: Int
    get() = getInt(PREFERENCES_LOGIN, -1)

val SharedPreferences.peanutToken: String
    get() = getString(PREFERENCES_PEANUT_TOKEN, "") ?: ""

val SharedPreferences.partnerToken: String
    get() = getString(PREFERENCES_PARTNER_TOKEN, "") ?: ""

val SharedPreferences.peanutBody: AuthorizationBody
    get() = AuthorizationBody(login, peanutToken)

val SharedPreferences.partnerBody: AuthorizationBody
    get() = AuthorizationBody(login, partnerToken)

val SharedPreferences.credentials
    get() = Credentials(
        getInt(PREFERENCES_LOGIN, -1),
        getString(PREFERENCES_PASSWORD, "") ?: "",
        getBoolean(PREFERENCES_REMEMBER, false),
        getString(PREFERENCES_PEANUT_TOKEN, "") ?: "",
        getString(PREFERENCES_PARTNER_TOKEN, "") ?: ""
    )