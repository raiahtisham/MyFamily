package com.example.myfamily

import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    private const val NAME = "MyFamilySharedPref"
    private const val MODE = Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun putBoolean(key: String, value: Boolean) {       // Saving boolean either true or false loged in or not

        preferences.edit().putBoolean(key, value).commit()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

}