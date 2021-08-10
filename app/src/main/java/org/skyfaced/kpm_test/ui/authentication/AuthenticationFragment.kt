package org.skyfaced.kpm_test.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentAuthenticationBinding
import org.skyfaced.kpm_test.model.Credentials
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.snack
import timber.log.Timber

class AuthenticationFragment :
    BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {
    private val viewModel by viewModel<AuthenticationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            val shakeAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)

            btnSignIn.setOnClickListener {
                val login = edtUsername.text?.toString()?.trim() ?: ""
                val password = edtPassword.text?.toString()?.trim() ?: ""

                val fields = listOf(login to tilUsername, password to tilPassword)

                fields.forEach(::showErrorOnNullOrEmptyField)

                val tils = listOf(tilUsername, tilPassword)
                if (tils.any { it.isErrorEnabled }) {
                    tils.filter { it.isErrorEnabled }.forEach { it.startAnimation(shakeAnimation) }
                    return@setOnClickListener
                }

                val body = AuthenticationBody(login.toInt(), password)
                viewModel.signIn(body)
            }
        }

        setupAuthenticationObservers()
    }

    private fun showErrorOnNullOrEmptyField(pair: Pair<String?, TextInputLayout>) {
        val (field, view) = pair

        when (field.isNullOrBlank()) {
            true -> view.error = getString(R.string.error_fill_empty_field)
            false -> view.isErrorEnabled = false
        }
    }

    private fun setupAuthenticationObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signIn.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { result ->
                binding {
                    when (result) {
                        is Result.Loading -> true
                        else -> false
                    }.let { isLoading ->
                        tilUsername.isEnabled = !isLoading
                        tilPassword.isEnabled = !isLoading
                        cbRemember.isEnabled = !isLoading
                        btnSignIn.isEnabled = !isLoading
                    }

                    when (result) {
                        is Result.Success -> {
                            Timber.d("Sign In Response ${result.data}")
                            val login = edtUsername.text.toString().trim().toInt()
                            val password = edtPassword.text.toString().trim()
                            val remember = cbRemember.isChecked
                            val (token1, token2) = result.data
                            val credentials = Credentials(login, password, remember, token1, token2)
                            viewModel.saveCredentials(credentials)
                        }
                        is Result.Error -> {
                            Timber.e(result.cause, result.message)
                            snack(getString(R.string.error_authorization))
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveCredentialsNotification.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { isSaved ->
                if (!isSaved) return@collect
                Timber.d("Credentials saved")
                val destination =
                    AuthenticationFragmentDirections.actionAuthenticationFragmentToGraphMain()
                findNavController().navigate(destination)
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentAuthenticationBinding {
        return FragmentAuthenticationBinding.inflate(inflater, parent, false)
    }
}