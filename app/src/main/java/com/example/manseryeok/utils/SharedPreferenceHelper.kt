package com.example.manseryeok.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceHelper {
    private const val PREF_APP = "PREF_MANSERYEOK"

    private const val useSatelliteMap = "SP_USE_SATELLITE_MAP" // boolean
    private const val expandSinsal = "SP_EXPAND_SINSAL" // boolean

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

    fun setExpandSinsal(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(expandSinsal, isEnable)
        editor.apply()
    }

    fun isExpandSinsal(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(expandSinsal, false)
    }
}