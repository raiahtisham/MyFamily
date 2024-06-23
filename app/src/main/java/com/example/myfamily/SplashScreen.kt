package com.example.myfamily

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// SplashScreen class responsible for displaying a splash screen
class SplashScreen : AppCompatActivity() {

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is logged in
        val isUserLoggedIn = SharedPref.getBoolean(PrefConstants.IS_USER_LOGGED_IN)

        // If user is logged in, start MainActivity and finish splash screen
        if (isUserLoggedIn){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{  // If user is not logged in, start LoginActivity and finish splash screen
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
