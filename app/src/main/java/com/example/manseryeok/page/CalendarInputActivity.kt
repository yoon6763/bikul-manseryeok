package com.example.manseryeok.page

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adevinta.leku.*
import com.adevinta.leku.locale.SearchZoneRect
import com.example.manseryeok.Utils.Utils
import com.example.manseryeok.databinding.ActivityCalendarInputBinding
import com.example.manseryeok.models.User
import com.google.android.gms.maps.model.LatLng


class CalendarInputActivity : AppCompatActivity() {
    companion object {
        const val PLACE_PICKER_REQUEST = 1
    }
    private val TAG = "CalendarInputActivity"
    private val binding by lazy { ActivityCalendarInputBinding.inflate(layoutInflater) }
    private val birth = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCalendarInput)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            etInputBirth.setOnClickListener {
                openBirthPicker()
            }

            etInputBirthTime.setOnClickListener {
                openBirthTimePicker()
            }

            etInputBirthPlace.setOnClickListener {
                openPlacePicker()
            }

            cbInputBirthTime.setOnCheckedChangeListener { compoundButton, b ->
                etInputBirthTime.isEnabled = !b
            }

            btnCalenderInputFinish.setOnClickListener {
                if (birth[Calendar.YEAR] >= 2101) {
                    Toast.makeText(
                        applicationContext,
                        "최대 2100년까지의 만세력 정보만 제공합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                nextPage()
            }
        }
    }

    // AIzaSyCBMxOrA67nzoiH0_jrhf9rqMyvaSkmFgo
    // AIzaSyDDT-Qk36iL8nPUx7n3Y1omsBHgYNZlBck

    private fun openPlacePicker() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withLocation(37.5666805, 126.9784147) // 디폴트 위치, 서울 시청
            .withGeolocApiKey("AIzaSyCBMxOrA67nzoiH0_jrhf9rqMyvaSkmFgo")
            .withSearchZone("kr_KR")
            .withSearchZone(SearchZoneRect(LatLng(26.525467, -18.910366), LatLng(43.906271, 5.394197)))
            .withDefaultLocaleSearchZone()
            .shouldReturnOkOnBackPressed()
            .withStreetHidden()
            .withCityHidden()
            .withZipCodeHidden()
            .withSatelliteViewHidden()
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .withUnnamedRoadHidden()
            .build(this@CalendarInputActivity)


        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST)
//        D/LATITUDE****: 37.474917421227204
//        D/LONGITUDE****: 126.95632204413414
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK")
            if (requestCode == 1) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val postalcode = data.getStringExtra(ZIPCODE)
                Log.d("POSTALCODE****", postalcode.toString())
                val bundle = data.getBundleExtra(TRANSITION_BUNDLE)
                Log.d("BUNDLE TEXT****", bundle?.getString("test")?: "")
                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                }
                val timeZoneId = data.getStringExtra(TIME_ZONE_ID)
                Log.d("TIME ZONE ID****", timeZoneId?:"")
                val timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME)
                Log.d("TIME ZONE NAME****", timeZoneDisplayName?:"")
            } else if (requestCode == 2) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
                Log.d("LekuPoi****", lekuPoi.toString())
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openBirthPicker() {
        val datePicker = DatePickerDialog(this@CalendarInputActivity,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                birth[Calendar.YEAR] = y
                birth[Calendar.MONTH] = m
                birth[Calendar.DAY_OF_MONTH] = d
                binding.etInputBirth.setText(Utils.dateSlideFormat.format(birth.timeInMillis))
            }, birth[Calendar.YEAR], birth[Calendar.MONTH] + 1, birth[Calendar.DAY_OF_MONTH]
        )

        datePicker.datePicker.calendarViewShown = false
        datePicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePicker.show()
    }

    private fun openBirthTimePicker() {
        val timePicker = TimePickerDialog(
            this@CalendarInputActivity,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                birth[Calendar.HOUR_OF_DAY] = h
                birth[Calendar.MINUTE] = m
                binding.etInputBirthTime.setText(Utils.timeFormat.format(birth.timeInMillis))
            }, birth[Calendar.HOUR_OF_DAY], birth[Calendar.MINUTE], false
        )
        timePicker.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePicker.show()
    }

    private fun nextPage() {
        binding.run {
//            var firstName: String?,
//            var lastName: String?,
//            var gender: Int, // 0 - 남자, 1 - 여자
//            var birth: String?, // yyyy-MM-dd
//            var birthTimeHour: Int?,
//            var birthTimeMin: Int?,
//            var birthPlace: String?
            birth.add(Calendar.MINUTE, -30)

            val date = if (rgCalType.checkedRadioButtonId == rbCalTypeMoon.id) {
                Utils.convertLunarToSolar(Utils.dateTimeSlideFormat.format(birth.timeInMillis))
            } else {
                Utils.dateTimeSlideFormat.format(birth.timeInMillis)
            }


            val userModel = User(
                etFirstName.text.toString(),
                etName.text.toString(),
                if (rgGender.checkedRadioButtonId == rbGenderMale.id) 0 else 1,
                !cbInputBirthTime.isChecked,
                date,
                etInputBirthPlace.text.toString()
            )


            val intent = Intent(this@CalendarInputActivity, CalendarActivity::class.java)
            intent.putExtra(Utils.INTENT_EXTRAS_USER, userModel)
            startActivity(intent)
            finish()
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