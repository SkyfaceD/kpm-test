package org.skyfaced.kpm_test.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentAuthenticationBinding
import org.skyfaced.kpm_test.ui.BaseFragment

class AuthenticationFragment :
    BaseFragment<FragmentAuthenticationBinding>(R.layout.fragment_authentication) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentAuthenticationBinding {
        return FragmentAuthenticationBinding.inflate(inflater, parent, false)
    }
}