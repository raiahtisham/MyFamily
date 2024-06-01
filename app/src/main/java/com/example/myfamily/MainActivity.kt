package com.example.myfamily

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener { menuItem ->          // It will be called when we will click any icon in bottom navigation
            when (menuItem.itemId) {
                R.id.nav_guard -> {

                    inflateFragment(GuardFragment.newInstance())

                }
                R.id.nav_home -> {

                    inflateFragment(HomeFragment.newInstance())

                }
                R.id.nav_home -> {

                    inflateFragment(DashboardFragment.newInstance())

                }
                R.id.nav_home -> {

                    inflateFragment(ProfileFragment.newInstance())

                }
            }

            true      // Selection in the navigation bar will be shown by true
        }
        bottomBar.selectedItemId = R.id.nav_home
    }

    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()
    }
}