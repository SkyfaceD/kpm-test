package org.skyfaced.kpm_test.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.skyfaced.kpm_test.network.ApplicationNetwork
import org.skyfaced.kpm_test.network.CabinetMicroServiceApi
import org.skyfaced.kpm_test.network.CabinetMicroServiceImpl
import org.skyfaced.kpm_test.utils.NetworkState
import org.skyfaced.kpm_test.utils.PREFERENCES_NAME

@ExperimentalSerializationApi
val applicationModule = module(createdAtStart = true) {
    single { sharedPreferences }

    single { networkState }

    single { json }

    single { ApplicationNetwork() }

    single<CabinetMicroServiceApi> { CabinetMicroServiceImpl() }
}

private val json: Json
    get() = Json

private val Scope.networkState: NetworkState
    get() {
        return NetworkState(
            androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }

private val Scope.sharedPreferences: SharedPreferences
    get() {
        return androidApplication().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }