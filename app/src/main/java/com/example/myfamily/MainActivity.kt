package com.example.myfamily

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Array of required permissions
    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS,
    )

    // Permission request code
    val permissionCode = 78

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enable edge-to-edge display
        setContentView(R.layout.activity_main)  // Set the activity layout

        // Check if all permissions are granted
        if (isAllPermissionsGranted()) {
            // Check if location is enabled
            if (isLocationEnabled(this)) {
                setUpLocationListener()  // Set up location listener
            } else {
                showGPSNotEnabledDialog(this)  // Show dialog to enable GPS
            }
        } else {
            askForPermission()  // Request necessary permissions
        }

        // Initialize the bottom navigation bar
        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        // Set the item selected listener for the bottom navigation bar
        bottomBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_guard -> {
                    inflateFragment(GuardFragment.newInstance())  // Load GuardFragment
                }
                R.id.nav_home -> {
                    inflateFragment(HomeFragment.newInstance())  // Load HomeFragment
                }
                R.id.nav_dashboard -> {
                    inflateFragment(MapsFragment())  // Load MapsFragment
                }
                R.id.nav_profile -> {
                    inflateFragment(ProfileFragment.newInstance())  // Load ProfileFragment
                }
            }
            true  // Indicate that the selection is handled
        }
        bottomBar.selectedItemId = R.id.nav_home  // Set the default selected item

        // Get current user info from FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = currentUser?.displayName.toString()
        val mail = currentUser?.email.toString()
        val phoneNumber = currentUser?.phoneNumber.toString()
        val imageUrl = currentUser?.photoUrl.toString()

        // Initialize Firestore
        val db = Firebase.firestore

        // Create a user map to store user data
        val user = hashMapOf(
            "name" to name,
            "mail" to mail,
            "phoneNumber" to phoneNumber,
            // "imageUrl" to imageUrl (commented out)
        )

        // Save user data to Firestore
        db.collection("users").document(mail).set(user)
            .addOnSuccessListener {
                // Handle success
            }.addOnFailureListener {
                // Handle failure
            }
    }

    // Set up the location listener
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Create location request with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Request location updates
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        // Log the location results
                        Log.d("Location89", "onLocationResult: latitude ${location.latitude}")
                        Log.d("Location89", "onLocationResult: longitude ${location.longitude}")

                        // Get current user email
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val mail = currentUser?.email.toString()

                        // Create a Firestore instance
                        val db = Firebase.firestore

                        // Create a map to store location data
                        val locationData = mutableMapOf<String, Any>(
                            "lat" to location.latitude.toString(),
                            "long" to location.longitude.toString(),
                        )

                        // Update Firestore document with location data
                        db.collection("users").document(mail).update(locationData)
                            .addOnSuccessListener {
                                // Handle success
                            }.addOnFailureListener {
                                // Handle failure
                            }
                    }
                }
            },
            Looper.myLooper()
        )
    }

    // Check if location services are enabled
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Show a dialog to prompt the user to enable GPS
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Enable GPS")
            .setMessage("required_for_this_app")
            .setCancelable(false)
            .setPositiveButton("enable_now") { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    // Check if all permissions are granted
    private fun isAllPermissionsGranted(): Boolean {
        for (item in permissions) {
            if (ContextCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // Request necessary permissions
    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    // Inflate the specified fragment
    private fun inflateFragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionCode) {
            if (allPermissionGranted()) {
                setUpLocationListener()  // Set up location listener if all permissions are granted
            } else {
                // Handle case where not all permissions are granted
            }
        }
    }

    // Open the camera (function not currently used in the onCreate method)
    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    // Check if all permissions are granted
    private fun allPermissionGranted(): Boolean {
        for (item in permissions) {
            if (ContextCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
