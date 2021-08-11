package org.skyfaced.kpm_test.ui.promotions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentPromotionsBinding
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.activitySnack
import timber.log.Timber

class PromotionsFragment : BaseFragment<FragmentPromotionsBinding>(R.layout.fragment_promotions) {
    private val viewModel by viewModel<PromotionsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.promotionsInfo.flowWithLifecycle(lifecycle).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Timber.d(result.data.toString())
                    }
                    is Result.Error -> {
                        Timber.e(result.cause, result.message)
                        activitySnack(getString(R.string.error_archive_fetching))
                    }
                }
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentPromotionsBinding {
        return FragmentPromotionsBinding.inflate(inflater, parent, false)
    }
}