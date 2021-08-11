package org.skyfaced.kpm_test.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.kpm_test.databinding.ItemArchiveBinding
import org.skyfaced.kpm_test.model.adapter.ArchiveItem

class ArchiveAdapter(
    private val onItemClick: (ArchiveItem) -> Unit,
) : ListAdapter<ArchiveItem, ArchiveAdapter.ArchiveViewHolder>(Companion) {
    class ArchiveViewHolder(private val binding: ItemArchiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArchiveItem, onItemClick: (ArchiveItem) -> Unit) {
            with(binding) {
                txtName.text = item.name
                txtCurrencyPair.text = item.currencyPair
                txtPrice.text = item.price
                txtMinPrice.text = item.minPrice
                txtMaxPrice.text = item.maxPrice
                txtActualTime.text = item.actualTime

                root.setOnClickListener {
                    if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                    onItemClick(item)
                }
            }
        }
    }

    companion object : DiffUtil.ItemCallback<ArchiveItem>() {
        //@formatter:off
        override fun areItemsTheSame(oldItem: ArchiveItem, newItem: ArchiveItem) =
            oldItem.actualTime   == newItem.actualTime   &&
            oldItem.name         == newItem.name         &&
            oldItem.currencyPair == newItem.currencyPair &&
            oldItem.price        == newItem.price        &&
            oldItem.minPrice     == newItem.minPrice     &&
            oldItem.maxPrice     == newItem.maxPrice
        //@formatter:on

        override fun areContentsTheSame(oldItem: ArchiveItem, newItem: ArchiveItem) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArchiveViewHolder(
        ItemArchiveBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}