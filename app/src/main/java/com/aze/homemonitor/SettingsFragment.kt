package com.aze.homemonitor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView

class SettingsFragment : Fragment() {

    var temperatureLabel : TextView? = null
    var tempLowRange : SeekBar? = null
    var tempHighRange : SeekBar? = null
    var humidityLabel : TextView? = null
    var humidityLowRange : SeekBar? = null
    var humidityHighRange : SeekBar? = null
    var motionSensorSwitch: Switch? = null
    var soundSensorSwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview : View = inflater.inflate(R.layout.fragment_settings, container, false)

        return rootview
    }

    companion object {
       private val TAG = "SettingsFragment"
    }
}