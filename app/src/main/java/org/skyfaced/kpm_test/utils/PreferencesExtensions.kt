package org.skyfaced.kpm_test.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import org.skyfaced.kpm_test.model.Credentials

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clear(commit: Boolean = false) {
    if (commit) {
        edit().clear().commit()
    } else {
        edit().clear().apply()
    }
}

val SharedPreferences.credentials
    get() = Credentials(
        getInt(PREFERENCES_LOGIN, -1),
        getString(PREFERENCES_PASSWORD, "") ?: "",
        getBoolean(PREFERENCES_REMEMBER, false),
        getString(PREFERENCES_PEANUT_TOKEN, "") ?: "",
        getString(PREFERENCES_PARTNER_TOKEN, "") ?: ""
    )