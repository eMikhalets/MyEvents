package com.emikhalets.mydates.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.ItemEventBinding
import com.emikhalets.mydates.utils.EventType
import com.emikhalets.mydates.utils.toDateString

class DayEventsAdapter(
    private val click: (Event) -> Unit
) : ListAdapter<Event, DayEventsAdapter.DayEventViewHolder>(DayEventsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        return DayEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DayEventViewHolder(private val binding: ItemEventBinding) :
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
                    info, item.date.toDateString("d MMMM")
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

    class DayEventsDiffCallback : DiffUtil.ItemCallback<Event>() {

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }
}