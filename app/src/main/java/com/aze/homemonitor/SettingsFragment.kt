package com.aze.homemonitor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class SettingsFragment : Fragment() {

    var temperatureLabel : TextView? = null
    var tempLowRange : SeekBar? = null
    var tempHighRange : SeekBar? = null
    var humidityLabel : TextView? = null
    var humidityLowRange : SeekBar? = null
    var humidityHighRange : SeekBar? = null
    var motionSensorSwitch: Switch? = null
    var soundSensorSwitch: Switch? = null

    var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview : View = inflater.inflate(R.layout.fragment_settings, container, false)

        temperatureLabel = rootview.findViewById(R.id.temperatureLabel)
        tempLowRange = rootview.findViewById(R.id.tempLowRange)
        tempHighRange = rootview.findViewById(R.id.tempHighRange)
        humidityLabel = rootview.findViewById(R.id.humidityLabel)
        humidityLowRange = rootview.findViewById(R.id.humidityLowRange)
        humidityHighRange = rootview.findViewById(R.id.humidityHighRange)
        motionSensorSwitch = rootview.findViewById(R.id.motionSensorSwitch)
        soundSensorSwitch = rootview.findViewById(R.id.soundSensorSwitch)

        activity?.let {
            homeMonitorLiveData = ViewModelProviders.of(it).get(HomeMonitorLiveDataModel::class.java)
        }

        homeMonitorLiveData!!.tempMinValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(MainActivity.TAG, " Fragment temperature Min " + t)
                    updateTemperatureMin(t)
                }
            }
        )

        homeMonitorLiveData!!.tempMaxValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(MainActivity.TAG, " Fragment temperature Max " + t)
                    updateTemperatureMax(t)
                }
            }
        )

        return rootview
    }

    fun updateTemperatureMin(minTemp : Int) {
        tempLowRange?.progress = minTemp
    }

    fun updateTemperatureMax(maxTemp : Int) {
        tempLowRange?.progress = maxTemp
    }

    companion object {
       private val TAG = "SettingsFragment"
    }
}