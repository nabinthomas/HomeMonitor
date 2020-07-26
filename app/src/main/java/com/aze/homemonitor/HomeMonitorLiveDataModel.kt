package com.aze.homemonitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeMonitorLiveDataModel : ViewModel() {
    val temperature = MutableLiveData<Int>()
    val humidity    = MutableLiveData<Int>()

}


