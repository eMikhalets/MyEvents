package com.emikhalets.mydates.ui.dates_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.DateItem
import com.emikhalets.mydates.databinding.ItemDateBinding
import com.emikhalets.mydates.utils.dateFormat

class DatesAdapter(private val click: (DateItem) -> Unit) :
    ListAdapter<DateItem, DatesAdapter.ViewHolder>(DatesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DateItem) {
            with(binding) {
                textName.text = item.name
                textInfo.text = root.context.getString(
                    R.string.dates_list_text_item_info,
                    item.date.dateFormat("d MMMM")
                )

                if (item.daysLeft == 0) textDaysLeft.text = root.context.resources.getString(
                    R.string.dates_list_today
                )
                else textDaysLeft.text = root.context.resources.getQuantityString(
                    R.plurals.dates_list_text_days_left,
                    item.daysLeft, item.daysLeft
                )

                imageForward.setOnClickListener { click.invoke(item) }
            }
        }
    }

    class DatesDiffCallback : DiffUtil.ItemCallback<DateItem>() {

        override fun areItemsTheSame(oldItem: DateItem, newItem: DateItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DateItem, newItem: DateItem): Boolean =
            oldItem == newItem
    }
}