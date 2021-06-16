package com.emikhalets.mydates.ui.events_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.ItemDateBinding
import com.emikhalets.mydates.databinding.ItemDateDividerBinding

class DatesAdapter(private val click: (GroupDateItem) -> Unit) :
    ListAdapter<GroupDateItem, RecyclerView.ViewHolder>(DatesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val binding = ItemDateDividerBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)
            }
            else -> {
                val binding = ItemDateBinding.inflate(inflater, parent, false)
                DateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as DateViewHolder).bind(getItem(position))
            1 -> (holder as DividerViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    inner class DateViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroupDateItem) {
            with(binding) {
                textName.text = item.name
                textInfo.text = root.context.getString(
                    R.string.dates_list_item_info,
                    item.date.dateFormat("d MMMM")
                )
                textAge.text = root.context.resources.getQuantityString(
                    R.plurals.dates_list_age,
                    item.age, item.age
                )

                if (item.daysLeft == 0) textDaysLeft.text = root.context.getString(
                    R.string.dates_list_today
                )
                else textDaysLeft.text = root.context.resources.getQuantityString(
                    R.plurals.dates_list_days_left,
                    item.daysLeft, item.daysLeft
                )

                imageForward.setOnClickListener { click.invoke(item) }
            }
        }
    }

    inner class DividerViewHolder(private val binding: ItemDateDividerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroupDateItem) {
            binding.textMonth.text =
                binding.root.context.resources.getStringArray(R.array.months)[item.month]
        }
    }

    class DatesDiffCallback : DiffUtil.ItemCallback<GroupDateItem>() {

        override fun areItemsTheSame(oldItem: GroupDateItem, newItem: GroupDateItem): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.month == newItem.month

        override fun areContentsTheSame(oldItem: GroupDateItem, newItem: GroupDateItem): Boolean =
            oldItem == newItem
    }
}