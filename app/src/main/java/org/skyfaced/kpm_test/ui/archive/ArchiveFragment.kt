package org.skyfaced.kpm_test.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentArchiveBinding
import org.skyfaced.kpm_test.ui.BaseFragment

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentArchiveBinding {
        return FragmentArchiveBinding.inflate(inflater, parent, false)
    }
}