package org.skyfaced.kpm_test.ui.promotions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.skyfaced.kpm_test.databinding.ItemPromotionsBinding
import org.skyfaced.kpm_test.model.adapter.PromotionsItem

class PromotionsAdapter(private val onItemClick: (link: String) -> Unit) :
    ListAdapter<PromotionsItem, PromotionsAdapter.PromotionsViewHolder>(Companion) {
    inner class PromotionsViewHolder(
        private val binding: ItemPromotionsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var link: String? = null

        init {
            binding.root.setOnClickListener {
                link?.let(onItemClick)
            }
        }

        fun bind(item: PromotionsItem) {
            link = item.link
            with(binding) {
                imgPromotions.load(item.image)
                txtTitle.text = item.title
                txtLabel.text = item.label
            }
        }
    }

    companion object : DiffUtil.ItemCallback<PromotionsItem>() {
        //@formatter:off
        override fun areItemsTheSame(oldItem: PromotionsItem, newItem: PromotionsItem) =
            oldItem.image == newItem.image &&
            oldItem.title == newItem.title &&
            oldItem.label == newItem.label
        //@formatter:on

        override fun areContentsTheSame(oldItem: PromotionsItem, newItem: PromotionsItem) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionsViewHolder {
        return PromotionsViewHolder(
            ItemPromotionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PromotionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}