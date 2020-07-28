package com.aze.homemonitor

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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


    fun connectFor(mainActivity: MainActivity, email: String?) {
        database = Firebase.database.reference
        homeMonitorLiveData = ViewModelProviders.of(mainActivity)
            .get(HomeMonitorLiveDataModel::class.java)

        val query = database.child("users").orderByChild("email").equalTo(email).limitToFirst(1)
        val stateChangelistener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userState in dataSnapshot.children) {
                    Log.d(TAG, "Data is " + userState.getValue().toString())
                    // post this data  to the Live data
                    homeMonitorLiveData?.alarmStatus?.postValue((userState?.getValue<UserState>() as UserState).alarmState)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        query.addValueEventListener(stateChangelistener)

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
    }

}