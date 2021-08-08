package org.skyfaced.kpm_test.di.modules

import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module
import org.skyfaced.kpm_test.network.ApplicationNetwork
import org.skyfaced.kpm_test.repository.PartnerAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.repository.PeanutAuthenticationRepositoryImpl

@ExperimentalSerializationApi
val repositoryModule = module {
    single {
        PeanutAuthenticationRepositoryImpl(get<ApplicationNetwork>().peanutApi())
    }

    single {
        PartnerAuthenticationRepositoryImpl(get<ApplicationNetwork>().partnerApi())
    }
}