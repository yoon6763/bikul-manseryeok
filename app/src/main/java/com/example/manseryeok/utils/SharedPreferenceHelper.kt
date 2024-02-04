package com.example.manseryeok.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceHelper {
    private const val PREF_APP = "PREF_MANSERYEOK"

    private const val useSatelliteMap = "SP_USE_SATELLITE_MAP" // boolean
    private const val expandSinsal = "SP_EXPAND_SINSAL" // boolean
    private const val useBirthDisplayASC = "SP_USE_BIRTH_DISPLAY_ASC" // boolean
    private const val termsAgree = "SP_TERMS_AGREE" // boolean

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

    fun setBirthDisplayASC(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(useBirthDisplayASC, isEnable)
        editor.apply()
    }

    fun isBirthDisplayASC(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(useBirthDisplayASC, false)
    }

    fun setTermsAgree(context: Context, isAgree: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(termsAgree, isAgree)
        editor.apply()
    }

    fun isTermsAgree(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(termsAgree, false)
    }
}