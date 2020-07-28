package com.aze.homemonitor.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserState {
    var email: String? = null
    var alarmState : Boolean? = false
    var enableSoundSensor : Boolean? = false
    var enableMotionSensor : Boolean? = false
    var humidityMinValue: Int? = 0
    var humidityMaxValue: Int? = 100
    var temperatureMinValue : Int? = 0
    var temperatureMaxValue : Int? = 120

}