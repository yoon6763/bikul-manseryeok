package com.example.manseryeok.page

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.manseryeok.R
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.adapter.ManseryeokSQLAdapter
import com.example.manseryeok.databinding.ActivityMainBinding
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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

        getHashKey()

    }

//    private fun initLoadDB() {
//        val mDBHelper = ManseryeokSQLAdapter(applicationContext)
//        mDBHelper.createDataBase()
//        mDBHelper.open()
//
//        val manList = mDBHelper.getTableData(2011, 2013)
//
//        mDBHelper.close()
//
//        manList!!.forEach {
//            Log.d("devvv","${it.sig}  ${it.sun_year}  ${it.sun_month}  ${it.sun_day}")
//        }
//    }



    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

    override fun onClick(p0: View?) {
        binding.run {
            when (p0?.id) {
                clCalendar.id -> startActivity(Intent(this@MainActivity, CalendarInputActivity::class.java))
                clCompass.id -> startActivity(Intent(this@MainActivity, CompassActivity::class.java))
            }
        }
    }
}