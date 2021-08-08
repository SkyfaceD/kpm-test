package org.skyfaced.kpm_test.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentStartBinding
import org.skyfaced.kpm_test.ui.BaseFragment

class StartFragment : BaseFragment<FragmentStartBinding>(R.layout.fragment_start) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            btnMain.setOnClickListener {
                val destination =
                    StartFragmentDirections.actionStartFragmentToMultipleBackStackGraph()
                findNavController().navigate(destination)
            }

            btnAuth.setOnClickListener {
                val destination =
                    StartFragmentDirections.actionStartFragmentToAuthenticationFragment()
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