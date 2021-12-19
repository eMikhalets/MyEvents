package com.emikhalets.mydates.ui.event_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.mydates.databinding.ItemContactBinding

class ContactsAdapter(
    private val phoneClick: (String) -> Unit,
    private val smsClick: (String) -> Unit,
    private val deleteClick: (String) -> Unit,
) : ListAdapter<String, ContactsAdapter.ContactViewHolder>(DatesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                textContact.text = item
                imagePhone.setOnClickListener { phoneClick.invoke(item) }
                imageSms.setOnClickListener { smsClick.invoke(item) }
                imageDelete.setOnClickListener { deleteClick.invoke(item) }
            }
        }
    }

    class DatesDiffCallback : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}