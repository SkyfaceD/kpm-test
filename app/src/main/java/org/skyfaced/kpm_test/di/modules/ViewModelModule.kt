package org.skyfaced.kpm_test.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.skyfaced.kpm_test.ui.authentication.AuthenticationViewModel

val viewModelModule = module {
    viewModel { AuthenticationViewModel(get(), get(), get()) }
}