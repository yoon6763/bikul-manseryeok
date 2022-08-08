package com.example.manseryeok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.manseryeok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


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
                clCalendar.id -> {

                }
            }
        }
    }
}