package com.emikhalets.mydates.utils

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.emikhalets.mydates.databinding.*
import com.emikhalets.mydates.utils.di.appComponent
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.PhotoPickerType
import com.emikhalets.mydates.utils.extentions.launchMainScope
import java.util.*

object AppDialogManager {

    fun showAddEventDialog(context: Context, callback: (EventType) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogAddEventBinding.inflate(LayoutInflater.from(context))
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

    fun showDatePickerDialog(context: Context, init: Long, callback: (Long) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(context))
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

    fun showTimePickerDialog(context: Context, callback: (hour: Int, minute: Int) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogTimePickerBinding.inflate(LayoutInflater.from(context))
        binding.root.alpha = 0f
        dialog.setContentView(binding.root)
        binding.timePicker.setIs24HourView(true)
        dialog.setCanceledOnTouchOutside(false)
        binding.root.animate().alpha(1f).setDuration(300).start()

        launchMainScope {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.timePicker.hour = context.appComponent.appPreferences.getNotificationHour()
                binding.timePicker.minute =
                    context.appComponent.appPreferences.getNotificationMinute()
            } else {
                @Suppress("DEPRECATION")
                binding.timePicker.currentHour =
                    context.appComponent.appPreferences.getNotificationHour()
                @Suppress("DEPRECATION")
                binding.timePicker.currentMinute =
                    context.appComponent.appPreferences.getNotificationMinute()
            }
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

    fun showDeleteDialog(context: Context, content: String, callback: () -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
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

    fun showPhotoPicker(context: Context, callback: (PhotoPickerType) -> Unit) {
        val dialog = Dialog(context)
        val binding = DialogPhotoPickerBinding.inflate(LayoutInflater.from(context))
        binding.root.alpha = 0f
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        binding.root.animate().alpha(1f).setDuration(300).start()

        binding.apply {
            btnTakePhoto.setOnClickListener {
                callback.invoke(PhotoPickerType.TAKE_PHOTO)
                dialog.dismiss()
            }

            btnSelectImage.setOnClickListener {
                callback.invoke(PhotoPickerType.SELECT_IMAGE)
                dialog.dismiss()
            }
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}