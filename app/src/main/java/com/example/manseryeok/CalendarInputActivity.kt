package com.example.manseryeok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.manseryeok.databinding.ActivityCalendarInputBinding

class CalendarInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}