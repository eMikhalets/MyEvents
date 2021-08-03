package com.emikhalets.mydates.utils

import android.app.Dialog
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.emikhalets.mydates.databinding.DialogAddEventBinding
import com.emikhalets.mydates.databinding.DialogConfirmBinding
import com.emikhalets.mydates.databinding.DialogDatePickerBinding
import com.emikhalets.mydates.databinding.DialogTimePickerBinding
import java.util.*

inline fun AppCompatActivity.startAddEventDialog(crossinline callback: (EventType) -> Unit) {
    val dialog = Dialog(this)
    val binding = DialogAddEventBinding.inflate(LayoutInflater.from(this))
    binding.root.alpha = 0f
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    binding.root.animate().alpha(1f).setDuration(300).start()

    binding.apply {
        cardBirthday.setOnClickListener {
            callback.invoke(EventType.BIRTHDAY)
            dialog.dismiss()
        }
        cardAnniversary.setOnClickListener {
            callback.invoke(EventType.ANNIVERSARY)
            dialog.dismiss()
        }
        imageClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    dialog.show()
    val window = dialog.window
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}

inline fun Fragment.startDatePickerDialog(init: Long, crossinline callback: (Long) -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(requireContext()))
    binding.root.alpha = 0f
    val calendar = Calendar.getInstance()
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    binding.root.animate().alpha(1f).setDuration(300).start()

    calendar.timeInMillis = init
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    binding.datePicker.init(year, month, day) { _, newYear, newMonth, newDay ->
        val picked = Calendar.getInstance()
        picked.set(newYear, newMonth, newDay)
        calendar.timeInMillis = picked.timeInMillis
    }

    binding.btnApply.setOnClickListener {
        callback.invoke(calendar.timeInMillis)
        dialog.dismiss()
    }
    binding.imageClose.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

inline fun Fragment.startTimePickerDialog(crossinline callback: (hour: Int, minute: Int) -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogTimePickerBinding.inflate(LayoutInflater.from(requireContext()))
    binding.root.alpha = 0f
    dialog.setContentView(binding.root)
    binding.timePicker.setIs24HourView(true)
    dialog.setCanceledOnTouchOutside(false)
    binding.root.animate().alpha(1f).setDuration(300).start()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        binding.timePicker.hour = Preferences.getNotificationHour(requireContext())
        binding.timePicker.minute = Preferences.getNotificationMinute(requireContext())
    } else {
        @Suppress("DEPRECATION")
        binding.timePicker.currentHour = Preferences.getNotificationHour(requireContext())
        @Suppress("DEPRECATION")
        binding.timePicker.currentMinute = Preferences.getNotificationMinute(requireContext())
    }

    var hour = 11
    var minute = 0
    binding.timePicker.setOnTimeChangedListener { _, h, m ->
        hour = h
        minute = m
    }

    binding.btnApply.setOnClickListener {
        callback.invoke(hour, minute)
        dialog.dismiss()
    }
    binding.imageClose.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

inline fun Fragment.startDeleteDialog(content: String, crossinline callback: () -> Unit) {
    val dialog = Dialog(requireContext())
    val binding = DialogConfirmBinding.inflate(LayoutInflater.from(requireContext()))
    binding.root.alpha = 0f
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    binding.root.animate().alpha(1f).setDuration(300).start()

    binding.textContent.text = content
    binding.btnNo.setOnClickListener { dialog.dismiss() }
    binding.btnYes.setOnClickListener {
        callback.invoke()
        dialog.dismiss()
    }

    dialog.show()
    val window = dialog.window
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}