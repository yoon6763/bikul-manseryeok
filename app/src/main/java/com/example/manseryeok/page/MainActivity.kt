package com.example.manseryeok.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.navy)

        binding.run {
            clCalendar.setOnClickListener(this@MainActivity)
            clCompass.setOnClickListener(this@MainActivity)
            clDatabase.setOnClickListener(this@MainActivity)
            clName.setOnClickListener(this@MainActivity)
            clMedia.setOnClickListener(this@MainActivity)
            clQuestion.setOnClickListener(this@MainActivity)
        }
    }


    override fun onClick(p0: View?) {
        binding.run {
            when (p0?.id) {
                clCalendar.id -> startActivity(Intent(this@MainActivity, CalendarInputActivity::class.java))
            }
        }
    }
}