package com.example.manseryeok.page

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.NumberPicker
import com.example.manseryeok.R

class NumberPickerDialog(context: Context) : Dialog(context) {

    interface OnConfirmListener {
        fun onConfirm(number: Int)
    }

    var onConfirmListener: OnConfirmListener? = null
    private var numberPicker: NumberPicker
    private var button: Button

    var maxValue = 0
        set(value) {
            field = value
            numberPicker.maxValue = value
        }

    var initialValue = 0
        set(value) {
            field = value
            numberPicker.value = value
        }

    var minValue = 0
        set(value) {
            field = value
            numberPicker.minValue = value
        }

    init {
        setContentView(R.layout.number_picker)

        numberPicker = findViewById(R.id.np_number)
        button = findViewById(R.id.btn_confirm)

        button.setOnClickListener {
            onConfirmListener?.onConfirm(numberPicker.value)
            dismiss()
        }
    }
}