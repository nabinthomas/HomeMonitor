package com.aze.homemonitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HomeMonitorLiveDataModel : ViewModel() {
    val temperature = MutableLiveData<Int>()
    val humidity    = MutableLiveData<Int>()
    val lastMotionDetected = MutableLiveData<String>()
    val lastSoundDetected = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val alarmStatus = MutableLiveData<Boolean>()
    val tempMinValue = MutableLiveData<Int>()
    val tempMaxValue = MutableLiveData<Int>()
    val humidityMinValue = MutableLiveData<Int>()
    val humidityMaxValue = MutableLiveData<Int>()
    val enableMotionSensor = MutableLiveData<Boolean>()
    val enableSoundSensor = MutableLiveData<Boolean>()
    val deviceToken = MutableLiveData<String>()
}


