package org.skyfaced.kpm_test.ui.profile

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentProfileBinding
import org.skyfaced.kpm_test.model.ProfileInfo
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.activitySnack
import org.skyfaced.kpm_test.utils.extensions.beautifyMoney
import org.skyfaced.kpm_test.utils.extensions.clear
import timber.log.Timber

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val preferences by inject<SharedPreferences>()
    private val viewModel by viewModel<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchProfileInfo()
            }

            btnSignOut.setOnClickListener {
                Timber.d("Sign Out")
                preferences.clear(true)

                val destination =
                    ProfileFragmentDirections.actionProfileFragmentToAuthenticationFragment()
                findNavController().navigate(destination)
            }
        }

        setupProfileInfoObserver()
    }

    private fun setupProfileInfoObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profileInfo.flowWithLifecycle(lifecycle).collect { result ->
                binding {
                    when (result) {
                        is Result.Loading -> true
                        else -> false
                    }.let { isRefreshing ->
                        swipeRefresh.isRefreshing = isRefreshing
                    }

                    when (result) {
                        is Result.Success -> {
                            Timber.d(result.data.toString())
                            fillViews(result.data)
                        }
                        is Result.Error -> {
                            Timber.e(result.cause, result.message)
                            activitySnack(getString(R.string.error_profile_fetching))
                        }
                    }
                }
            }
        }
    }

    private fun FragmentProfileBinding.fillViews(profileInfo: ProfileInfo) {
        with(profileInfo) {
            val verificationDrawable = ContextCompat.getDrawable(
                requireContext(),
                if (verificationLevel == 0) R.drawable.ic_lock_open
                else R.drawable.ic_lock
            )
            imgVerificationLevel.setImageDrawable(verificationDrawable)
            txtUsername.text = username
            txtPhoneNumber.text = phoneNumber

            txtBalance.text = balance.beautifyMoney(currencyCode.symbol)
            txtEquity.text =
                getString(R.string.placeholder_equity, equity.beautifyMoney(currencyCode.symbol))
            txtFreeMargin.text = getString(R.string.placeholder_free_margin,
                freeMargin.beautifyMoney(currencyCode.symbol))

            txtCurrentTradesCountValue.text = currentTradesCount
            txtTotalTradesCountValue.text = totalTradesCount
            grpCheckCurrentTrades.isVisible = isAnyOpenTrades
            dividerHorizontal.isInvisible = !isAnyOpenTrades
            if (isAnyOpenTrades) {
                cardTrades.setOnClickListener {
                    activitySnack("В разработке")
                }
            }
            txtVolumeCurrentTradesCountValue.text = currentTradesVolume
            txtVolumeTotalTradesCountValue.text = totalTradesVolume

            if (countryCode.isNotEmpty()) {
                val flagUri = Uri.parse("https://flagcdn.com/${countryCode}.svg")
                imgCountry.load(flagUri)
            } else {
                //TODO
                imgCountry.load(R.drawable.fl_empty)
            }
            txtCity.text = city
            txtAddress.text = getString(R.string.placeholder_address, address)
            txtZipCode.text = getString(R.string.placeholder_zip_code, zipCode)
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, parent, false)
    }
}