package org.skyfaced.kpm_test.ui.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import org.skyfaced.kpm_test.R
import org.skyfaced.kpm_test.databinding.FragmentStockBinding
import org.skyfaced.kpm_test.ui.BaseFragment

class StockFragment : BaseFragment<FragmentStockBinding>(R.layout.fragment_stock) {
    override fun setupBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentStockBinding {
        return FragmentStockBinding.inflate(inflater, parent, false)
    }
}