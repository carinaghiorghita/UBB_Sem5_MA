package com.ubb.mobile

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.ubb.mobile.core.Properties
import com.ubb.mobile.core.TAG
import com.ubb.mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var light: Sensor? = null

    //private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        showAllSensors()
        checkSensor()
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        //connectivityManager = getSystemService(android.net.ConnectivityManager::class.java)
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onStart() {
//        super.onStart()
//        connectivityManager.registerDefaultNetworkCallback(networkCallback)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onStop() {
//        super.onStop()
//        connectivityManager.unregisterNetworkCallback(networkCallback)
//    }

//    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            Properties.instance.internetActive.postValue(true)
//            runOnUiThread {
//                networkTxt.text = getString(R.string.active_network)
//                networkIc.setImageResource(R.drawable.ic_active_network)
//            }
//        }
//
//        override fun onLost(network: Network) {
//            Properties.instance.internetActive.postValue(false)
//            runOnUiThread {
//                networkTxt.text = getString(R.string.inactive_network)
//                networkIc.setImageResource(R.drawable.ic_inactive_network)
//            }
//        }
//    }

    private fun showAllSensors(){
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.d(TAG,"showAllSensors")
        deviceSensors.forEach {
            Log.d(TAG, it.name+ " "+it.vendor+" "+it.version)
        }
    }

    private fun checkSensor() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            val gravSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_GRAVITY)
            val sensor = gravSensors.firstOrNull { it.vendor.contains("AOSP") && it.version == 3 }
            sensor?.let { Log.d(TAG, "Check sensor " + it.name) }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(TAG, "onAccuracyChanged $accuracy");
    }

    override fun onSensorChanged(event: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        val lux = event.values[0]
        // Do something with this sensor value.
        Log.d(TAG, "onSensorChanged $lux");
    }

    override fun onResume() {
        super.onResume()
        light?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}