package com.emikhalets.mydates.ui.events_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.ItemEventBinding
import com.emikhalets.mydates.databinding.ItemEventDividerBinding
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.extentions.formatDate
import com.emikhalets.mydates.utils.extentions.setImageUri

class EventsAdapter(private val click: (Event) -> Unit) :
    ListAdapter<Event, RecyclerView.ViewHolder>(DatesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = ItemEventDividerBinding.inflate(inflater, parent, false)
                DividerViewHolder(binding)
            }
            else -> {
                val binding = ItemEventBinding.inflate(inflater, parent, false)
                EventViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as DividerViewHolder).bind(getItem(position))
            else -> (holder as EventViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).eventType
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            with(binding) {
                imagePhoto.setImageUri(item.imageUri, root.context.contentResolver)

                textAge.isGone = item.withoutYear

                textName.text = item.fullName()

                val date = item.date.formatDate("d MMMM")
                textDate.text = when (item.eventType) {
                    EventType.ANNIVERSARY.value ->
                        root.context.getString(R.string.anniversary_date, date)
                    EventType.BIRTHDAY.value ->
                        root.context.getString(R.string.birthday_date, date)
                    else -> date
                }

                textDaysLeft.text = when (item.daysLeft) {
                    0 -> root.context.getString(R.string.today)
                    1 -> root.context.getString(R.string.tomorrow)
                    else -> root.context.resources.getQuantityString(
                        R.plurals.days_left,
                        item.daysLeft, item.daysLeft
                    )
                }

                if (item.eventType != 0) {
                    root.setOnClickListener { click.invoke(item) }
                }
            }
        }
    }

    inner class DividerViewHolder(private val binding: ItemEventDividerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            binding.textMonth.text =
                binding.root.context.resources.getStringArray(R.array.months)[item.age]
        }
    }

    class DatesDiffCallback : DiffUtil.ItemCallback<Event>() {

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }
}