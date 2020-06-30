package com.iplato.spacex.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context?) {

    private val PREFS_NAME = "spacex"

    val sharedPref: SharedPreferences =
        context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueBoolean(KEY_NAME: String): Boolean = sharedPref.getBoolean(KEY_NAME, false)

}