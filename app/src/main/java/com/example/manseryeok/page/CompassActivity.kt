package com.example.manseryeok.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.manseryeok.databinding.ActivityCompassBinding
import net.daum.mf.map.api.MapView

class CompassActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCompassBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCompass)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            val mapView = MapView(this@CompassActivity)
            mvCompass.addView(mapView)
            mapView.setMapRotationAngle(30f,true)



        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}