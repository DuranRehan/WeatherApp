package g56055.mobg.meteoapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import g56055.mobg.meteoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkPermissionsLocation()
        checkLocationEnabled()
        checkWifiOrDataIsEnabled()
        drawerLayout = binding.drawerLayout
        // Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                setLockDrawer(true)
                NavigationUI.setupActionBarWithNavController(this, navController)
            } else {
                setLockDrawer(false)
                //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
            }
        }
    }

    private fun checkPermissionsLocation() {
        if (hasPermissions()) {
            Log.d("MainActivity", "hasPermissions")

        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("MainActivity", "shouldShowRequestPermissionRationale")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    10002
                )
            } else {
                Log.d("MainActivity", "requestPermissions")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    10001
                )
            }
        }

    }

    private fun checkLocationEnabled() {
        val lm : LocationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGpsEnabled = false
        var isNetworkEnable = false

        try {
            isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            isNetworkEnable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if (!isGpsEnabled && !isNetworkEnable) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.gps_network_not_enabled_title))
                .setPositiveButton(getString(R.string.gps_not_enabled_positive_button)
                ) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    finish()
                }
                .setNegativeButton(getString(R.string.gps_not_enabled_negative_button)) { _, _ ->
                    finish()
                }
                .show()
        }
    }
    private fun checkWifiOrDataIsEnabled(){
        val cm : ConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (networkCapabilities == null) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.wifi_network_not_enabled_title))
                .setPositiveButton(getString(R.string.wifi_not_enabled_positive_button)
                ) { _, _ ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    finish()
                }
                .setNegativeButton(getString(R.string.wifi_not_enabled_negative_button)) { _, _ ->
                    finish()
                }
                .show()
        }

    }
    private fun hasPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun setLockDrawer(lock: Boolean) {
        if (lock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


}