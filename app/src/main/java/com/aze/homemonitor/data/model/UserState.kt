package com.aze.homemonitor.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserState {
    var email: String? = null
    var alarmState : Boolean? = false
}