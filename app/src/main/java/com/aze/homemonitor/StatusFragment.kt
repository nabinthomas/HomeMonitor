package com.aze.homemonitor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class StatusFragment : Fragment() {
    var temperatureTextView: TextView? = null
    var humidityTextView: TextView? = null
    var motionEventTextView : TextView? = null
    var soundDetectionTextView : TextView? = null
    var alarmSwitch: Switch? = null

    var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootview : View = inflater.inflate(R.layout.fragment_status, container, false)

        temperatureTextView = rootview.findViewById(R.id.temperatureTextView)
        humidityTextView = rootview.findViewById(R.id.humidityTextView)
        motionEventTextView = rootview.findViewById(R.id.motionEventTextView)
        soundDetectionTextView = rootview.findViewById(R.id.soundDetectionTextView)
        alarmSwitch = rootview.findViewById(R.id.alarmSwitch)

        activity?.let {
            homeMonitorLiveData = ViewModelProviders.of(it).get(HomeMonitorLiveDataModel::class.java)
        }

        homeMonitorLiveData!!.temperature.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(MainActivity.TAG, " Fragment temperature " + t)
                    updateTemperature(t)
                }
            }
        )

        homeMonitorLiveData!!.humidity.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(MainActivity.TAG, " Fragment humidity " + t)
                    updateHumidity(t)
                }
            }
        )

        homeMonitorLiveData!!.lastMotionDetected.observe(this,
            object: Observer<String> {
                override fun onChanged(t: String) {
                    Log.d(MainActivity.TAG, " Fragment lastMotionDetected " + t)
                    updateLastMotionDetected(t)
                }
            }
        )
        homeMonitorLiveData!!.lastSoundDetected.observe(this,
            object: Observer<String> {
                override fun onChanged(t: String) {
                    Log.d(MainActivity.TAG, " Fragment lastSoundDetected " + t)
                    updateLastSoundDetected(t)
                }
            }
        )

        homeMonitorLiveData!!.alarmStatus.observe(this,
            object: Observer<Boolean> {
                override fun onChanged(t: Boolean) {
                    Log.d(MainActivity.TAG, " Fragment AlarmStatus " + t)
                    updateAlarmStatus(t)
                }
            }
        )

        homeMonitorLiveData!!.userEmail.observe(this,
            object: Observer<String> {
                override fun onChanged(email: String) {
                    Log.d(MainActivity.TAG, " Fragment user Email " + email)
                    registerForAlarmStatusChange(email)
                }
            }
        )

        alarmSwitch?.setOnClickListener {

            Log.d(MainActivity.TAG, "Switch is + " + (it as Switch).isChecked)
            this.homeMonitorLiveData?.alarmStatus?.postValue((it as Switch).isChecked)
        }

        return rootview
    }

    fun updateTemperature(temperature: Int){
        temperatureTextView?.setText(Integer.toString(temperature))
    }

    fun updateHumidity(humidity: Int){
        humidityTextView?.setText(Integer.toString(humidity))
    }

    fun updateLastMotionDetected(time: String){
        motionEventTextView?.setText("Movement Detected @ "  + time)
    }

    fun updateLastSoundDetected(time: String){
        soundDetectionTextView?.setText("Sound Detected @ "  + time)
        alarmSwitch?.isChecked = !alarmSwitch?.isChecked!!

    }
    fun updateAlarmStatus(alarmStatus: Boolean){
        alarmSwitch?.isChecked = alarmStatus
    }

    fun registerForAlarmStatusChange(email: String) {

    }
}