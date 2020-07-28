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

        /// Wire up everything for temperature range for notification
        homeMonitorLiveData!!.tempMinValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(TAG, " Fragment temperature Min " + t)
                    updateTemperatureMin(t)
                }
            }
        )

        homeMonitorLiveData!!.tempMaxValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(TAG, " Fragment temperature Max " + t)
                    updateTemperatureMax(t)
                }
            }
        )

        tempLowRange?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                homeMonitorLiveData!!.tempMinValue.postValue(tempLowRange?.progress)
            }
            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        tempHighRange?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                homeMonitorLiveData!!.tempMinValue.postValue(tempLowRange?.progress)
            }
            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }

        })


        // Now with Humidity Controls
        homeMonitorLiveData!!.humidityMinValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(TAG, " Fragment humidity Min " + t)
                    updateHumidityMin(t)
                }
            }
        )

        homeMonitorLiveData!!.humidityMaxValue.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(TAG, " Fragment Humidity Max " + t)
                    updateHumidityMax(t)
                }
            }
        )

        humidityLowRange?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                homeMonitorLiveData!!.humidityMinValue.postValue(humidityLowRange?.progress)
            }
            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        humidityHighRange?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                homeMonitorLiveData!!.humidityMaxValue.postValue(humidityHighRange?.progress)
            }
            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }

        })

        // Now with motion sensor
        homeMonitorLiveData!!.enableMotionSensor.observe(this,
            object: Observer<Boolean> {
                override fun onChanged(t: Boolean) {
                    Log.d(TAG, " Fragment Motion Sensor " + t)
                    updateMotionSensorStatus(t)
                }
            }
        )
        motionSensorSwitch?.setOnClickListener {

            Log.d(MainActivity.TAG, "Switch is + " + (it as Switch).isChecked)
            this.homeMonitorLiveData?.enableMotionSensor?.postValue((it as Switch).isChecked)
        }

        // Now with Sound Sensor
        homeMonitorLiveData!!.enableSoundSensor.observe(this,
            object: Observer<Boolean> {
                override fun onChanged(t: Boolean) {
                    Log.d(TAG, " Fragment Sound Sensor " + t)
                    updateSoundSensorStatus(t)
                }
            }
        )
        soundSensorSwitch?.setOnClickListener {

            Log.d(MainActivity.TAG, "Switch is + " + (it as Switch).isChecked)
            this.homeMonitorLiveData?.enableSoundSensor?.postValue((it as Switch).isChecked)
        }

        // Init the defaults for now. Once live data is hooked up with Firebase, this is not needed
        updateHumidityRangeText()
        updateTemperatureRangeText()

        return rootview
    }

    fun updateTemperatureMin(minTemp : Int) {
        tempLowRange?.progress = minTemp
        updateTemperatureRangeText()
    }

    fun updateTemperatureMax(maxTemp : Int) {
        tempHighRange?.progress = maxTemp
        updateTemperatureRangeText()
    }

    fun updateTemperatureRangeText(){
        temperatureLabel?.setText("Humidity: Notify when < " + tempLowRange?.progress + " OR > " + tempHighRange?.progress)
    }


    fun updateHumidityMin(minHumidity : Int) {
        humidityLowRange?.progress = minHumidity
        updateHumidityRangeText()
    }

    fun updateHumidityMax(maxHumidity : Int) {
        humidityHighRange?.progress = maxHumidity
        updateHumidityRangeText()
    }

    fun updateHumidityRangeText(){
        humidityLabel?.setText("Humidity: Notify when < " + humidityLowRange?.progress + " OR > " + humidityHighRange?.progress)
    }

    fun updateMotionSensorStatus(enabled : Boolean){
        motionSensorSwitch?.isChecked = enabled
    }

    fun updateSoundSensorStatus(enabled : Boolean){
        soundSensorSwitch?.isChecked = enabled
    }

    companion object {
       private val TAG = "SettingsFragment"
    }
}