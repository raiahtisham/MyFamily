package com.example.myfamily

import android.content.Context
import android.content.SharedPreferences

// Singleton object to manage shared preferences
object SharedPref {
    // Constant for the shared preferences file name
    private const val NAME = "MyFamilySharedPref"
    // Constant for the mode of the shared preferences (private mode)
    private const val MODE = Context.MODE_PRIVATE
    // Lateinit property for SharedPreferences object
    lateinit var preferences: SharedPreferences

    // Initialization function to set up the shared preferences
    fun init(context: Context) {
        // Get the shared preferences using the application context
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    // Function to save a boolean value in shared preferences
    fun putBoolean(key: String, value: Boolean) {
        // Edit the preferences and commit the change to save the boolean value
        preferences.edit().putBoolean(key, value).commit()
    }

    // Function to retrieve a boolean value from shared preferences
    fun getBoolean(key: String): Boolean {
        // Get the boolean value with the specified key, return false if not found
        return preferences.getBoolean(key, false)
    }
}

// Explanation:
// This file is essential for managing shared preferences within the app. Shared preferences are used to store small amounts of data as key-value pairs. This is useful for persisting user settings and preferences across app sessions.
// - The `init` function initializes the shared preferences with a specific file name and mode.
// - The `putBoolean` function allows saving a boolean value (like whether the user is logged in or not).
// - The `getBoolean` function retrieves the boolean value associated with a given key.
// Using shared preferences helps maintain user state and settings, enhancing the user experience by remembering their preferences even after the app is closed.
