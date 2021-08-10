package org.skyfaced.kpm_test.ui.archive

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentArchiveBinding
import org.skyfaced.kpm_test.model.ArchiveInfo
import org.skyfaced.kpm_test.model.CurrencyPairItem
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.activitySnack
import org.skyfaced.kpm_test.utils.extensions.lazySafetyNone
import org.skyfaced.kpm_test.utils.extensions.snack
import timber.log.Timber

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    private val viewModel by viewModel<ArchiveViewModel>()

    private val archiveAdapter by lazySafetyNone {
        ArchiveAdapter { item ->
            binding.coordinator.snack("Id ${item.id}")
        }
    }
    private val currencyPairAdapter by lazySafetyNone {
        CurrencyPairAdapter(::currencyPairItemClicked)
    }

    private val bottomSheetBehavior by lazySafetyNone {
        BottomSheetBehavior.from(binding.bottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> btnFilters.hide()
                        BottomSheetBehavior.STATE_HIDDEN -> btnFilters.show()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Ignore
                }
            })

            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }

            btnFilters.setOnClickListener {
                btnFilters.hide()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            btnCloseFilters.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            btnAcceptFilters.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            currencyPairAdapter.initialize(
                listOf(
                    CurrencyPairItem("EURUSD"),
                    CurrencyPairItem("GBPUSD"),
                    CurrencyPairItem("USDJPY"),
                    CurrencyPairItem("USDCHF"),
                    CurrencyPairItem("USDCAD"),
                    CurrencyPairItem("AUDUSD"),
                    CurrencyPairItem("NZDUSD")
                )
            )
            recyclerCurrencyPair.adapter = currencyPairAdapter

            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            recyclerCurrencyPair.layoutManager = layoutManager
        }
        setupRecycler()
        setupSearchObserver()
    }

    private fun currencyPairItemClicked(item: CurrencyPairItem, position: Int) {
        currencyPairAdapter.update(item, position)
    }

    private fun setupRecycler() {
        binding {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerArchive.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY && btnFilters.isExtended) {
                        btnFilters.shrink()
                    }

                    if (scrollY < oldScrollY && !btnFilters.isExtended) {
                        btnFilters.extend()
                    }
                }
            } else {
                //TODO test on sdk < 23
                recyclerArchive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(view, dx, dy)
                        if (dy > 0 && btnFilters.isExtended) {
                            btnFilters.shrink()
                        }

                        if (dy < 0 && !btnFilters.isExtended) {
                            btnFilters.extend()
                        }
                    }
                })
            }
            recyclerArchive.adapter = archiveAdapter
        }
    }

    private fun setupSearchObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.archiveInfo.flowWithLifecycle(lifecycle).collect { result ->
                binding {
                    swipeRefresh.isRefreshing = when (result) {
                        is Result.Loading -> true
                        else -> false
                    }

                    when (result) {
                        is Result.Success -> {
                            Timber.d(result.data.toString())
                            archiveAdapter.submitList(result.data.map(ArchiveInfo::toArchiveItem))
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

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentArchiveBinding {
        return FragmentArchiveBinding.inflate(inflater, parent, false)
    }
}