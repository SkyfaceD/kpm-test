package org.skyfaced.kpm_test.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.kpm_test.databinding.ItemChipFilterBinding
import org.skyfaced.kpm_test.model.CurrencyPairItem

class CurrencyPairAdapter(
    private val onItemClick: (item: CurrencyPairItem, position: Int) -> Unit,
) : RecyclerView.Adapter<CurrencyPairAdapter.FilterViewHolder>() {
    private val currencyPairs = mutableListOf<CurrencyPairItem>()

    fun initialize(items: List<CurrencyPairItem>) {
        currencyPairs.addAll(items)
        notifyItemRangeInserted(0, items.size)
    }

    fun update(item: CurrencyPairItem, position: Int) {
        currencyPairs.removeAt(position)
        currencyPairs.add(position, item.copy(state = !item.state))
        notifyItemChanged(position)
    }

    inner class FilterViewHolder(private val binding: ItemChipFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var _currencyPairItem: CurrencyPairItem? = null
        private var _position: Int? = null

        init {
            binding.chip.setOnClickListener {
                onItemClick(_currencyPairItem!!, _position!!)
            }
        }

        fun bind(item: CurrencyPairItem, position: Int) {
            _currencyPairItem = item
            _position = position
            with(binding.chip) {
                text = item.text
                isSelected = item.state
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemChipFilterBinding.inflate(
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