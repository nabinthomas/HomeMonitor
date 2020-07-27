package com.aze.homemonitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HomeMonitorLiveDataModel : ViewModel() {
    val temperature = MutableLiveData<Int>()
    val humidity    = MutableLiveData<Int>()
    val lastMotionDetected = MutableLiveData<String>()
    val lastSoundDetected = MutableLiveData<String>()
}


