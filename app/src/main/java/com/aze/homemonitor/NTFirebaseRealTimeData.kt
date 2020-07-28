package com.aze.homemonitor

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.aze.homemonitor.data.model.UserState
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class NTFirebaseRealTimeData{
    companion object {
        const val TAG = "NTFirebaseRealTimeData"
    }
    private lateinit var database: DatabaseReference

    private var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    private var dataStateChangeListener : ValueEventListener? = null
    private var dataQuery : Query? = null

    fun disconnect() {
        if (dataStateChangeListener != null) {
            dataQuery?.removeEventListener(dataStateChangeListener!!)
        }
    }

    fun connectFor(mainActivity: MainActivity, email: String?) {
        database = Firebase.database.reference
        homeMonitorLiveData = ViewModelProviders.of(mainActivity)
            .get(HomeMonitorLiveDataModel::class.java)

        // Wire Up from Database to UI
        val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
        val stateChangelistener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userState in dataSnapshot.children) {
                    Log.d(TAG, "Data is " + userState.getValue().toString())
                    // post this data  to the Live data
                    // 1. Alarm Status
                    homeMonitorLiveData?.alarmStatus?.postValue((userState?.getValue<UserState>() as UserState).alarmState)
                    // 2. Sound Sensor
                    homeMonitorLiveData?.enableSoundSensor?.postValue((userState?.getValue<UserState>() as UserState).enableSoundSensor)
                    // 3. Motion Sensor
                    homeMonitorLiveData?.enableMotionSensor?.postValue((userState?.getValue<UserState>() as UserState).enableMotionSensor)
                    // 4. Humidity min range
                    homeMonitorLiveData?.humidityMinValue?.postValue((userState?.getValue<UserState>() as UserState).humidityMinValue)
                    // 5. Humidity max range
                    homeMonitorLiveData?.humidityMaxValue?.postValue((userState?.getValue<UserState>() as UserState).humidityMaxValue)
                    // 6. Temperature min range
                    homeMonitorLiveData?.tempMinValue?.postValue((userState?.getValue<UserState>() as UserState).temperatureMinValue)
                    // 7. Temperature max range
                    homeMonitorLiveData?.tempMaxValue?.postValue((userState?.getValue<UserState>() as UserState).temperatureMaxValue)
                    // 8. Current Temperature
                    homeMonitorLiveData?.temperature?.postValue((userState?.getValue<UserState>() as UserState).temperature)
                    // 9. Current Humidity
                    homeMonitorLiveData?.humidity?.postValue((userState?.getValue<UserState>() as UserState).humidity)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        query.addValueEventListener(stateChangelistener)
        dataQuery = query; // save to disconnect later
        dataStateChangeListener = stateChangelistener


        // Wire Up from UI to Database
        // 1. Alarm Status
        homeMonitorLiveData!!.alarmStatus.observe(mainActivity,
            object: Observer<Boolean> {
                override fun onChanged(status: Boolean) {
                    Log.d(TAG, "Current Alarm Status " + status)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("alarmState").setValue(status)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )
        // 2. Sound Sensor
        homeMonitorLiveData!!.enableSoundSensor.observe(mainActivity,
            object: Observer<Boolean> {
                override fun onChanged(status: Boolean) {
                    Log.d(TAG, "Current SoundSensor Status " + status)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("enableSoundSensor").setValue(status)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 3. Motion Sensor
        homeMonitorLiveData!!.enableMotionSensor.observe(mainActivity,
            object: Observer<Boolean> {
                override fun onChanged(status: Boolean) {
                    Log.d(TAG, "Current MotionSensor Status " + status)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("enableMotionSensor").setValue(status)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 3. Humidity Min Value
        homeMonitorLiveData!!.humidityMinValue.observe(mainActivity,
            object: Observer<Int> {
                override fun onChanged(value: Int) {
                    Log.d(TAG, "Current Humidity Min " + value)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("humidityMinValue").setValue(value)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 4. Humidity Max Value
        homeMonitorLiveData!!.humidityMaxValue.observe(mainActivity,
            object: Observer<Int> {
                override fun onChanged(value: Int) {
                    Log.d(TAG, "Current Humidity Max " + value)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("humidityMaxValue").setValue(value)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 5. Temperature Min Value
        homeMonitorLiveData!!.tempMinValue.observe(mainActivity,
            object: Observer<Int> {
                override fun onChanged(value: Int) {
                    Log.d(TAG, "Current Temperature Min " + value)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("temperatureMinValue").setValue(value)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 4. Temperature Max Value
        homeMonitorLiveData!!.tempMaxValue.observe(mainActivity,
            object: Observer<Int> {
                override fun onChanged(value: Int) {
                    Log.d(TAG, "Current Temperature Max " + value)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("temperatureMaxValue").setValue(value)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )

        // 5. Device Registration Token
        homeMonitorLiveData!!.deviceToken.observe(mainActivity,
            object: Observer<String> {
                override fun onChanged(value: String) {
                    Log.d(TAG, "Current Device Token  " + value)
                    val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
                    val stateChangelistener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.children.count() == 0)
                            {
                                // This is the first time login for the user.
                                // Create this entry
                                var newUser = UserState()
                                newUser.email = email

                                // Get the firebase token and add it here too


                                database.child("users").push().setValue(newUser);

                            }
                            for (userState in dataSnapshot.children) {
                                Log.d(TAG, "While Writing new State : Data was  (key, value) = " + userState.key + "," + userState.getValue().toString())
                                // set the new value
                                database.child("users").child(userState.key!!).child("deviceToken").setValue(value)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    query.addListenerForSingleValueEvent(stateChangelistener)
                }
            }
        )
    }

}