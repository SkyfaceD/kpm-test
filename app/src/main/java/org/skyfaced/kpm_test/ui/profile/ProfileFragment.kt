package org.skyfaced.kpm_test.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentProfileBinding
import org.skyfaced.kpm_test.ui.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, parent, false)
    }
}