package org.skyfaced.kpm_test.ui.archive

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
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

class ArchiveFragment : BaseFragment<FragmentArchiveBinding>(R.layout.fragment_archive) {
    private val viewModel by viewModel<ArchiveViewModel>()

    private val adapter by lazySafetyNone {
        ArchiveAdapter { item ->
            binding.coordinator.snack("Id ${item.id}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }

            btnFilters.setOnClickListener {

            }
        }
        setupRecycler()
        setupSearchObserver()
    }

    private fun setupRecycler() {
        binding {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recycler.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY && btnFilters.isExtended) {
                        btnFilters.shrink()
                    }

                    if (scrollY < oldScrollY && !btnFilters.isExtended) {
                        btnFilters.extend()
                    }
                }
            } else {
                //TODO test on sdk < 23
                recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0 && btnFilters.isExtended) {
                            btnFilters.shrink()
                        }

                        if (dy < 0 && !btnFilters.isExtended) {
                            btnFilters.extend()
                        }
                    }
                })
            }
            recycler.adapter = adapter
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
                            adapter.submitList(result.data.map(ArchiveInfo::toArchiveItem))
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