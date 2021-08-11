package org.skyfaced.kpm_test.ui.archive

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentArchiveBinding
import org.skyfaced.kpm_test.model.ArchiveInfo
import org.skyfaced.kpm_test.ui.BaseFragment
import org.skyfaced.kpm_test.utils.Result
import org.skyfaced.kpm_test.utils.extensions.activitySnack
import org.skyfaced.kpm_test.utils.extensions.lazySafetyNone
import org.skyfaced.kpm_test.utils.extensions.snack
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    private val viewModel by viewModel<ArchiveViewModel>()

    private val archiveAdapter by lazySafetyNone {
        ArchiveAdapter { item ->
            binding.coordinator.snack("Id ${item.id}")
        }
    }
    private val currencyPairAdapter by lazySafetyNone {
        CurrencyPairAdapter(::currencyPairAdapterItemClick)
    }
    private val bottomSheetBehavior by lazySafetyNone {
        BottomSheetBehavior.from(binding.bottomSheet)
    }

    //FIXME Leak
    private val dateRangePicker by lazySafetyNone {
        MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Выберите даты")
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            restoreViewsVisibility()

            swipeRefresh.setOnRefreshListener {
                if (viewModel.dates.value.first == -1L) {
                    swipeRefresh.isRefreshing = false
                    return@setOnRefreshListener
                }
                viewModel.searchArchiveInfo()
            }

            btnOpenFilters.setOnClickListener {
                viewModel.toggleBottomSheet(true)
            }

            btnCloseFilters.setOnClickListener {
                viewModel.toggleBottomSheet(false)
            }

            btnAcceptFilters.setOnClickListener {
                validateFilters()
            }

            dateRangePicker.addOnPositiveButtonClickListener(::onDateSelected)
            btnDateFrom.setOnClickListener(::showDateRangePicker)
            btnDateTo.setOnClickListener(::showDateRangePicker)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.dates.flowWithLifecycle(lifecycle).collect(::updateDates)
            }
        }
        setupBottomSheet()
        setupCurrencyRecycler()
        setupArchiveRecycler()
        setupArchiveObserver()
    }

    private fun restoreViewsVisibility() {
        binding {
            imgStartSearch.isVisible = viewModel.dates.value.first == -1L
            lblStartSearch.isVisible = viewModel.dates.value.first == -1L
            lblCurrencyPairError.isVisible = viewModel.currencyPairs.value.none { it.state }
        }
    }

    private fun validateFilters() {
        binding {
            var hasErrors = false

            Timber.e(viewModel.currencyPairs.value.joinToString("\n"))
            if (viewModel.currencyPairs.value.none { it.state }) {
                binding.lblCurrencyPairError.isVisible = true
                hasErrors = true
            }

            if (viewModel.dates.value.first == -1L) {
                binding.tilDateFrom.error = getString(R.string.error_choose_date)
                binding.tilDateTo.error = getString(R.string.error_choose_date)
                hasErrors = true
            }

            if (hasErrors) return@binding

            imgStartSearch.isVisible = false
            lblStartSearch.isVisible = false
            viewModel.toggleBottomSheet(false)
            viewModel.searchArchiveInfo()
        }
    }

    private fun updateDates(dates: Pair<Long, Long>) {
        if (dates.first == -1L) return
        binding {
            val (from, to) = dates
            btnDateFrom.setText(from.beautifyDate())
            btnDateTo.setText(to.beautifyDate())
        }
    }

    //FIXME Locale
    private fun Long.beautifyDate(): String {
        return SimpleDateFormat(
            "dd MMMM yyyy",
            Locale("ru", "RU")
        ).format(Date(this))
    }

    private fun showDateRangePicker(view: View) {
        binding.tilDateFrom.isErrorEnabled = false
        binding.tilDateTo.isErrorEnabled = false
        dateRangePicker.show(requireActivity().supportFragmentManager, TAG)
    }

    private fun onDateSelected(dates: Pair<Long, Long>) {
        viewModel.saveDates(dates)
    }

    private fun toggleBottomSheet(isExpanded: Boolean) {
        when (isExpanded) {
            true -> {
                binding.btnOpenFilters.hide()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            false -> {
                binding.btnOpenFilters.show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun currencyPairAdapterItemClick(position: Int) {
        binding.lblCurrencyPairError.isVisible = false
        viewModel.updateCurrencyPairs(position)
    }

    private fun setupBottomSheet() {
        binding {
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    Timber.e("State changed $newState")
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED ->
                            viewModel.toggleBottomSheet(true)
                        BottomSheetBehavior.STATE_HIDDEN ->
                            viewModel.toggleBottomSheet(false)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Ignore
                }
            })
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.bottomSheetState
                    .flowWithLifecycle(lifecycle)
                    .collect(::toggleBottomSheet)
            }
        }
    }

    private fun setupCurrencyRecycler() {
        binding {
            currencyPairAdapter.initialize(viewModel.currencyPairs.value)
            recyclerCurrencyPair.adapter = currencyPairAdapter

            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            recyclerCurrencyPair.layoutManager = layoutManager

            recyclerCurrencyPair.setHasFixedSize(true)
        }
    }

    private fun setupArchiveRecycler() {
        binding {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerArchive.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY && btnOpenFilters.isExtended) {
                        btnOpenFilters.shrink()
                    }

                    if (scrollY < oldScrollY && !btnOpenFilters.isExtended) {
                        btnOpenFilters.extend()
                    }
                }
            } else {
                //TODO test on sdk < 23
                recyclerArchive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(view, dx, dy)
                        if (dy > 0 && btnOpenFilters.isExtended) {
                            btnOpenFilters.shrink()
                        }

                        if (dy < 0 && !btnOpenFilters.isExtended) {
                            btnOpenFilters.extend()
                        }
                    }
                })
            }
            recyclerArchive.adapter = archiveAdapter
        }
    }

    private fun setupArchiveObserver() {
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
                            imgNotFound.isVisible = result.data.isEmpty()
                            lblNotFound.isVisible = result.data.isEmpty()
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

    override fun onStart() {
        super.onStart()
        toggleBottomSheet(viewModel.bottomSheetState.value)
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentArchiveBinding {
        return FragmentArchiveBinding.inflate(inflater, parent, false)
    }
}