package org.skyfaced.kpm_test.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.skyfaced.kpm_test.ui.archive.ArchiveViewModel
import org.skyfaced.kpm_test.ui.authentication.AuthenticationViewModel
import org.skyfaced.kpm_test.ui.profile.ProfileViewModel
import org.skyfaced.kpm_test.ui.promotions.PromotionsViewModel

val viewModelModule = module {
    viewModel { AuthenticationViewModel(get(), get(), get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { ArchiveViewModel(get()) }

    viewModel { PromotionsViewModel(get()) }
}