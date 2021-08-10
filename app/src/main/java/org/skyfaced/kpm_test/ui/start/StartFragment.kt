package org.skyfaced.kpm_test.ui.start

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentStartBinding
import org.skyfaced.kpm_test.model.Credentials
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.ui.authentication.AuthenticationViewModel
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.credentials
import org.skyfaced.kpm_test.utils.snack
import timber.log.Timber

class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {
    private val preferences by inject<SharedPreferences>()
    private val authViewModel by viewModel<AuthenticationViewModel>()

    private val credentials by lazy { preferences.credentials }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (credentials.password.isEmpty()) {
            Timber.d("User not logged")
            val destination =
                StartFragmentDirections.actionStartFragmentToAuthenticationFragment()
            findNavController().navigate(destination)
        } else {
            Timber.d("User logged")
            setupAuthenticationObservers()

            val (login, password) = credentials
            val body = AuthenticationBody(login, password)
            authViewModel.signIn(body)
        }
    }

    private fun setupAuthenticationObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.signIn.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { result ->
                binding {
                    when (result) {
                        is Result.Loading -> true
                        else -> false
                    }.let { isLoading ->
                        pbSignIn.isVisible = isLoading
                    }

                    when (result) {
                        is Result.Success -> {
                            Timber.d("Sign In Response ${result.data}")
                            val (login, password, remember) = credentials
                            val (token1, token2) = result.data
                            val credentials = Credentials(login, password, remember, token1, token2)
                            authViewModel.saveCredentials(credentials)
                        }
                        is Result.Error -> {
                            Timber.d(result.cause, result.message)
                            snack(getString(R.string.error_authorization))

                            val destination =
                                StartFragmentDirections.actionStartFragmentToAuthenticationFragment()
                            findNavController().navigate(destination)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.saveCredentialsNotification.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isSaved ->
                if (!isSaved) return@collect
                Timber.d("Credentials saved")
                val destination = StartFragmentDirections.actionStartFragmentToGraphMain()
                findNavController().navigate(destination)
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentStartBinding {
        return FragmentStartBinding.inflate(inflater, parent, false)
    }
}