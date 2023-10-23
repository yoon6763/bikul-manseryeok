package com.example.manseryeok.models

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.manseryeok.utils.Utils
import java.util.Calendar

class UserInputViewModel : ViewModel() {
    var id = MutableLiveData<Long>()
    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var gender = MutableLiveData<Int>(0)

    var year = MutableLiveData<Int>(-1)
    var month = MutableLiveData<Int>(-1)
    var day = MutableLiveData<Int>(-1)
    var hour = MutableLiveData<Int>(-1)
    var minute = MutableLiveData<Int>(-1)

    var birthLabel = MutableLiveData<String>("")
    var birthTimeLabel = MutableLiveData<String>("")
    var isIncludeTime = MutableLiveData<Boolean>(false)


    init {
        val today = Calendar.getInstance()
        year.value = today.get(Calendar.YEAR)
        month.value = today.get(Calendar.MONTH) + 1
        day.value = today.get(Calendar.DAY_OF_MONTH)
    }

    fun onGenderButtonClick(value: Int) {
        gender.value = value
    }

    fun openBirthPicker(view: View) {
        val context = view.context

        DatePickerDialog(
            context,
            R.style.Theme_Holo_Light_Dialog_MinWidth, { datePicker, y, m, d ->
                year.value = y
                month.value = m + 1
                day.value = d

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year.value!!)
                    set(Calendar.MONTH, month.value!! - 1)
                    set(Calendar.DAY_OF_MONTH, day.value!!)
                }

                birthLabel.value = Utils.dateSlideFormat.format(calendar.timeInMillis)
            }, year.value!!, month.value!! - 1, day.value!!
        ).apply {
            datePicker.calendarViewShown = false
            window!!.setBackgroundDrawableResource(R.color.transparent)
            show()
        }
    }

    fun openBirthTimePicker(view: View) {
        val context = view.context

        TimePickerDialog(
            context,
            R.style.Theme_Holo_Light_Dialog_MinWidth,
            TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                hour.value = h
                minute.value = m

                val time = String.format("%02d", hour.value) + " : " + String.format("%02d", minute.value)
                birthTimeLabel.value = time
            }, hour.value!!, minute.value!!, false
        ).apply {
            window!!.setBackgroundDrawableResource(android.R.color.transparent)
            show()
        }
    }


    fun isValid() {

    }
}