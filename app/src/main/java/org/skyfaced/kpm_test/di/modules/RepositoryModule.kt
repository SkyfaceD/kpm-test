package org.skyfaced.kpm_test.di.modules

import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module
import org.skyfaced.kpm_test.network.ApplicationNetwork
import org.skyfaced.kpm_test.repository.archive.ArchiveRepository
import org.skyfaced.kpm_test.repository.archive.ArchiveRepositoryImpl
import org.skyfaced.kpm_test.repository.authentication.PartnerAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.repository.authentication.PeanutAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.repository.profile.ProfileRepository
import org.skyfaced.kpm_test.repository.profile.ProfileRepositoryImpl
import org.skyfaced.kpm_test.repository.promotions.PromotionsRepository
import org.skyfaced.kpm_test.repository.promotions.PromotionsRepositoryImpl

@ExperimentalSerializationApi
val repositoryModule = module {
    single {
        PeanutAuthenticationRepositoryImpl(get<ApplicationNetwork>().peanutApi())
    }

    single {
        PartnerAuthenticationRepositoryImpl(get<ApplicationNetwork>().partnerApi())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(get<ApplicationNetwork>().peanutApi(), get())
    }

    single<ArchiveRepository> {
        ArchiveRepositoryImpl(get<ApplicationNetwork>().partnerApi(), get())
    }

    single<PromotionsRepository> {
        PromotionsRepositoryImpl(get(), get())
    }
}