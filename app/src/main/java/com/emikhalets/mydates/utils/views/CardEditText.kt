package com.emikhalets.mydates.utils.views

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import com.emikhalets.mydates.R
import com.emikhalets.mydates.utils.extentions.setDrawableStart
import com.google.android.material.resources.MaterialResources.getDimensionPixelSize
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CardEditText(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(context, attrs) {

    private val cardView: CardView
    private val inputLayoutView: TextInputLayout
    private val editTextView: TextInputEditText

    var text: String = ""
        set(value) {
            editTextView.setText(value)
            field = value
        }

    var error: String? = null
        set(value) {
            inputLayoutView.error = value
            field = value
        }

    init {
        inflate(context, R.layout.layout_card_edit_text, this)

        cardView = findViewById(R.id.card)
        inputLayoutView = findViewById(R.id.text_input_layout)
        editTextView = findViewById(R.id.edit_text)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardEditText)
        inputLayoutView.hint = attributes.getString(R.styleable.CardEditText_android_hint)
        editTextView.setRawInputType(
            attributes.getInt(R.styleable.CardEditText_android_inputType, 1)
        )
        editTextView.isCursorVisible = attributes.getBoolean(
            R.styleable.CardEditText_android_cursorVisible, true
        )
        editTextView.isFocusable = attributes.getBoolean(
            R.styleable.CardEditText_android_focusable, true
        )

        val drawableEnd = attributes.getDrawable(R.styleable.CardEditText_android_drawableEnd)
        drawableEnd?.setBounds(
            0,
            0,
            attributes.getDimensionPixelSize(
                R.styleable.CardEditText_drawableEndWidth, drawableEnd.intrinsicWidth
            ),
            attributes.getDimensionPixelSize(
                R.styleable.CardEditText_drawableEndHeight, drawableEnd.intrinsicWidth
            )
        )
        editTextView.setCompoundDrawables(null, null, drawableEnd, null)

        this.setOnClickListener {}

        attributes.recycle()
    }

    fun setOnClickListener(block: () -> Unit) {
        editTextView.setOnClickListener { block() }
    }

    fun doAfterTextChanged(block: (text: Editable?) -> Unit) {
        editTextView.doAfterTextChanged { block(it) }
    }
}