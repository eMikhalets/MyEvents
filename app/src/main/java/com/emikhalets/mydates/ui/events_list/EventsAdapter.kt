package com.emikhalets.mydates.ui.events_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.ItemEventBinding
import com.emikhalets.mydates.databinding.ItemEventDividerBinding
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.dateFormat

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
                var info = 0
                when (item.eventType) {
                    EventType.ANNIVERSARY.value -> {
                        info = R.string.anniversary_date
                        imagePhoto.setImageResource(R.drawable.ic_anniversary)
                    }
                    EventType.BIRTHDAY.value -> {
                        info = R.string.birthday_date
                        imagePhoto.setImageResource(R.drawable.ic_birthday)
                    }
                }

                if (item.withoutYear) textAge.visibility = View.GONE
                else textAge.visibility = View.VISIBLE

                textName.text = item.fullName()
                textInfo.text = root.context.getString(
                    info, item.date.dateFormat("d MMMM")
                )
                textAge.text = root.context.resources.getQuantityString(
                    R.plurals.age,
                    item.age, item.age
                )

                when (item.daysLeft) {
                    0 -> textDaysLeft.text = root.context.getString(R.string.today)
                    1 -> textDaysLeft.text = root.context.getString(R.string.tomorrow)
                    else -> textDaysLeft.text = root.context.resources.getQuantityString(
                        R.plurals.days_left,
                        item.daysLeft, item.daysLeft
                    )
                }

                if (item.eventType != 0) root.setOnClickListener { click.invoke(item) }
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