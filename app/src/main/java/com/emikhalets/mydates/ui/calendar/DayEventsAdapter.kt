package com.emikhalets.mydates.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.R
import com.emikhalets.mydates.data.database.entities.Event
import com.emikhalets.mydates.databinding.ItemEventBinding
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.extentions.formatDate
import com.emikhalets.mydates.utils.extentions.setImageUri

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

                textAge.text = root.context.resources.getQuantityString(
                    R.plurals.age,
                    item.age, item.age
                )

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

    class DayEventsDiffCallback : DiffUtil.ItemCallback<Event>() {

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }
}