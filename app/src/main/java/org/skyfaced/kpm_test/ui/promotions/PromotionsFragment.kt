package org.skyfaced.kpm_test.ui.promotions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
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
import org.skyfaced.kpm_test.utils.extensions.lazySafetyNone
import timber.log.Timber

class PromotionsFragment : BaseFragment<FragmentPromotionsBinding>(R.layout.fragment_promotions) {
    private val viewModel by viewModel<PromotionsViewModel>()

    private val adapter by lazySafetyNone { PromotionsAdapter(::onPromotionClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            swipeRefresh.setOnRefreshListener {
                viewModel.getPromotionsInfo()
            }

            recycler.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.promotionsInfo.flowWithLifecycle(lifecycle).collect { result ->
                    swipeRefresh.isRefreshing = when (result) {
                        is Result.Loading -> true
                        else -> false
                    }

                    when (result) {
                        is Result.Success -> {
                            Timber.d(result.data.toString())
                            adapter.submitList(result.data)
                        }
                        is Result.Error -> {
                            Timber.e(result.cause, result.message)
                            activitySnack(getString(R.string.error_archive_fetching))
                        }
                    }
                }
            }
        }
    }

    private fun onPromotionClick(link: String) {
        try {
            Intent(ACTION_VIEW, Uri.parse(link)).apply {
                addCategory(CATEGORY_BROWSABLE)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        } catch (e: ActivityNotFoundException) {
            activitySnack(getString(R.string.error_cannot_open_promotion))
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentPromotionsBinding {
        return FragmentPromotionsBinding.inflate(inflater, parent, false)
    }
}