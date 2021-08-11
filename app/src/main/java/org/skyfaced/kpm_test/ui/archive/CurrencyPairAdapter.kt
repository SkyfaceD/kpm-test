package org.skyfaced.kpm_test.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.kpm_test.databinding.ItemChipCurrencyPairBinding
import org.skyfaced.kpm_test.model.adapter.CurrencyPairItem

class CurrencyPairAdapter(
    private val onItemClick: (position: Int) -> Unit,
) : RecyclerView.Adapter<CurrencyPairAdapter.FilterViewHolder>() {
    private val currencyPairs = mutableListOf<CurrencyPairItem>()

    fun initialize(items: List<CurrencyPairItem>) {
        currencyPairs.addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    inner class FilterViewHolder(private val binding: ItemChipCurrencyPairBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var _position: Int? = null

        init {
            binding.chip.setOnClickListener {
                onItemClick(_position!!)
            }
        }

        fun bind(item: CurrencyPairItem, position: Int) {
            _position = position
            with(binding.chip) {
                text = item.text
                isChecked = item.state
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemChipCurrencyPairBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(currencyPairs[position], position)
    }

    override fun getItemCount() = currencyPairs.size
}