package com.example.manseryeok.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceHelper {
    private val PREF_APP = "PREF_MANSERYEOK"

    private const val useSummertime = "SP_USE_SUMMERTIME" // boolean
    private const val useTokyoTime = "SP_USE_TOKYO_TIME" // boolean
    private const val useSatelliteMap = "SP_USE_SATELLITE_MAP" // boolean

    fun setSatelliteMapEnable(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(useSatelliteMap, isEnable)
        editor.apply()
    }

    fun isSatelliteMapEnable(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(useSatelliteMap, true)
    }


    // 서머타임 사용 유무
    fun setSummertimeEnable(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(useSummertime, isEnable)
        editor.apply()
    }

    fun isSummertimeEnable(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(useSummertime, false)
    }

    fun setTokyoTimeEnable(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(useTokyoTime, isEnable)
        editor.apply()
    }

    fun isTokyoTimeEnable(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(useTokyoTime, false)
    }
}