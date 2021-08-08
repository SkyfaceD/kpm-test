package org.skyfaced.kpm_test

import android.app.Application
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.skyfaced.kpm_test.di.modules.applicationModule
import org.skyfaced.kpm_test.di.modules.repositoryModule
import org.skyfaced.kpm_test.di.modules.viewModelModule

@ExperimentalSerializationApi
class KPMTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KPMTestApp)
            modules(applicationModule, repositoryModule, viewModelModule)
        }
    }
}