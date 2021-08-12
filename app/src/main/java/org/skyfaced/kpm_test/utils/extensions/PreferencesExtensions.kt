package org.skyfaced.kpm_test.utils.extensions

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.core.content.edit
import org.skyfaced.kpm_test.model.Credentials
import org.skyfaced.kpm_test.model.network.body.AuthorizationBody
import org.skyfaced.kpm_test.utils.*

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clear(commit: Boolean = false) {
    edit(commit) {
        clear()
    }
}

val SharedPreferences.login: Int
    get() = getInt(PREFERENCES_LOGIN, -1)

val SharedPreferences.peanutToken: String
    get() = getString(PREFERENCES_PEANUT_TOKEN, "") ?: ""

fun SharedPreferences.savePeanutToken(token: String) {
    edit(true) {
        putString(PREFERENCES_PEANUT_TOKEN, token)
    }
}

val SharedPreferences.partnerToken: String
    get() = getString(PREFERENCES_PARTNER_TOKEN, "") ?: ""

fun SharedPreferences.savePartnerToken(token: String) {
    edit(true) {
        putString(PREFERENCES_PARTNER_TOKEN, token)
    }
}

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

fun SharedPreferences.clearCredentials() {
    edit(true) {
        remove(PREFERENCES_LOGIN)
        remove(PREFERENCES_PASSWORD)
        remove(PREFERENCES_REMEMBER)
        remove(PREFERENCES_PEANUT_TOKEN)
        remove(PREFERENCES_PARTNER_TOKEN)
    }
}