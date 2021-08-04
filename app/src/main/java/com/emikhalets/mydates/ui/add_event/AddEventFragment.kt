package com.emikhalets.mydates.ui.add_event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.mydates.R
import com.emikhalets.mydates.databinding.FragmentAddEventBinding
import com.emikhalets.mydates.utils.*
import com.emikhalets.mydates.utils.enums.EventType
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeImage
import com.emikhalets.mydates.utils.enums.EventType.Companion.getTypeName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private val binding by viewBinding(FragmentAddEventBinding::bind)
    private val viewModel: AddEventVM by viewModels()
    private val args: AddEventFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareEventData()
        clickListeners()
        observe()
    }

    private fun prepareEventData() {
        binding.inputDate.setText(viewModel.date.formatDate())
        setViewsForEventType(args.eventType)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.inputDate.setOnClickListener {
            startDatePickerDialog(viewModel.date) { timestamp ->
                viewModel.date = timestamp
                binding.inputDate.setDate(viewModel.date, binding.checkYear.isChecked)
            }
        }
        binding.checkYear.setOnCheckedChangeListener { _, isChecked ->
            binding.inputDate.setDate(viewModel.date, isChecked)
        }
        binding.btnSave.setOnClickListener {
            onSaveClick()
        }
        binding.root.setOnTouchListener { _, _ ->
            hideSoftKeyboard()
            clearFocus()
            false
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect { renderState(it) }
        }
    }

    private fun onSaveClick() {
        binding.layInputName.error = null
        val name = binding.inputName.text.toString()
        val lastname = binding.inputLastname.text.toString()
        val middleName = binding.inputMiddleName.text.toString()
        val withoutYear = binding.checkYear.isChecked
        viewModel.saveNewEvent(
            eventType = args.eventType,
            name = name,
            lastname = lastname,
            middleName = middleName,
            withoutYear = withoutYear
        )
    }

    private fun renderState(state: AddEventState) {
        when (state) {
            is AddEventState.Error -> {
                toast(state.message)
            }
            AddEventState.EmptyNameError -> {
                binding.layInputName.error = getString(R.string.required_field)
            }
            AddEventState.Added -> {
                navigateBack()
            }
            AddEventState.Init -> {
            }
        }
    }

    private fun setViewsForEventType(eventType: EventType) {
        binding.textTitle.apply {
            text = eventType.getTypeName(requireContext())
            setDrawableStart(eventType.getTypeImage())
        }

        if (eventType == EventType.ANNIVERSARY) {
            binding.apply {
                cardLastname.visibility = View.GONE
                cardMiddleName.visibility = View.GONE
            }
        }
    }

    private fun clearFocus() {
        binding.apply {
            inputName.clearFocus()
            inputLastname.clearFocus()
            inputMiddleName.clearFocus()
        }
    }
}