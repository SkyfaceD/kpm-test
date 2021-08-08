package org.skyfaced.kpm_test.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentProfileBinding
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.clear

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val preferences by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignOut.setOnClickListener {
            preferences.clear(true)

            val destination =
                ProfileFragmentDirections.actionProfileFragmentToAuthenticationFragment()
            findNavController().navigate(destination)
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, parent, false)
    }
}