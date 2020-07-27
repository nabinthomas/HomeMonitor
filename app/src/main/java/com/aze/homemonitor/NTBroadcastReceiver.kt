package com.aze.homemonitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import org.json.JSONObject

class NTBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "NTBroadcastReceiver"
    }

    private var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    fun init(mainActivity: MainActivity) {

        homeMonitorLiveData = ViewModelProviders.of(mainActivity)
            .get(HomeMonitorLiveDataModel::class.java)
    }

    override fun onReceive(context: Context?, intent: Intent) {
        try {
            if(intent.getAction().equals("com.aze.homemonitor.STATUS_NOTIFICATION")) {
                val value = intent.getStringExtra("HomeMonitorStatusUpdateJson")
                Log.d(TAG, value.toString())
                val statusJson = JSONObject(value.toString())

                var temperature = statusJson.get("temperature")
                var humidity = statusJson.get("humidity")
                var lastMotionDetected = statusJson.get("lastMotionDetected")
                var lastSoundDetected = statusJson.get("lastSoundDetected")

                homeMonitorLiveData?.temperature?.postValue(temperature as Int?)
                homeMonitorLiveData?.humidity?.postValue(humidity as Int?)
                homeMonitorLiveData?.lastMotionDetected?.postValue(lastMotionDetected as String?)
                homeMonitorLiveData?.lastSoundDetected?.postValue(lastSoundDetected as String?)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}