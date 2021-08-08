package org.skyfaced.kpm_test.di.modules

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.skyfaced.kpm_test.network.ApplicationNetwork
import org.skyfaced.kpm_test.utils.PREFERENCES_NAME

val applicationModule = module(createdAtStart = true) {
    single { sharedPreferences }

    single { ApplicationNetwork() }
}

private val Scope.sharedPreferences: SharedPreferences
    get() {
        return androidApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }