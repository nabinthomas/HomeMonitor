package com.aze.homemonitor

import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {

    private var currentuser: FirebaseUser? = null

    private var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    public var statusFragment: StatusFragment? = null

    private var receiver: NTBroadcastReceiver ? = null

    private var ntFireBaseRealtimeData: NTFirebaseRealTimeData ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentuser = null
        setContentView(R.layout.activity_main)

        receiver = NTBroadcastReceiver()
        receiver!!.init(this)

        registerReceiver(receiver, IntentFilter("com.aze.homemonitor.STATUS_NOTIFICATION"))

        ntFireBaseRealtimeData = NTFirebaseRealTimeData()

        // setupFakeNotifications()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.statusMenu -> {
                showStatus()
                true
            }
            R.id.settingsMenu -> {
                showSettings()
                true
            }
            R.id.logoutMenu -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (currentuser != null)
        {
            menu?.findItem(R.id.statusMenu)?.setVisible(true)
            menu?.findItem(R.id.settingsMenu)?.setVisible(true)
            menu?.findItem(R.id.logoutMenu)?.setVisible(true)
            menu?.findItem(R.id.logoutMenu)?.setTitle("Logout " + currentuser?.email)
        }
        else
        {
            menu?.findItem(R.id.statusMenu)?.setVisible(false)
            menu?.findItem(R.id.settingsMenu)?.setVisible(false)
            menu?.findItem(R.id.logoutMenu)?.setVisible(false)
            menu?.findItem(R.id.logoutMenu)?.setTitle("Logout ")
        }

        return super.onPrepareOptionsMenu(menu)
    }

    fun showStatus() {
        if (currentuser != null) {
            invalidateOptionsMenu()
            findNavController(R.id.nav_host_fragment).navigate(R.id.statusFragment)
        }
    }

    fun showSettings() {
        if (currentuser != null) {
            invalidateOptionsMenu()
            findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)

        }
    }

    fun logout() {
        homeMonitorLiveData?.deviceToken?.postValue("")

        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                setUser(null)
                invalidateOptionsMenu()
                findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)

            }
    }

    fun setUser(user: FirebaseUser?){
        currentuser = user
        if (user != null){
            homeMonitorLiveData = ViewModelProviders.of(this)
                .get(HomeMonitorLiveDataModel::class.java)

            // these two data prints are only for debug. Not really used.

            homeMonitorLiveData!!.temperature.observe(this,
                object: Observer<Int> {
                    override fun onChanged(t: Int) {
                        Log.d(TAG, "temperature " + t)
                    }
                }
            )

            homeMonitorLiveData!!.humidity.observe(this,
                object: Observer<Int> {
                    override fun onChanged(t: Int) {
                        Log.d(TAG, "humidity " + t)
                    }
                }
            )

            homeMonitorLiveData!!.userEmail.observe(this,
                object: Observer<String> {
                    override fun onChanged(email: String) {
                        Log.d(TAG, "Current User email " + email)
                    }
                }
            )

            homeMonitorLiveData!!.alarmStatus.observe(this,
                object: Observer<Boolean> {
                    override fun onChanged(status: Boolean) {
                        Log.d(TAG, "Current Alarm Status " + status)
                    }
                }
            )
        }
        else {
            homeMonitorLiveData?.temperature?.removeObservers(this)

        }

        if (currentuser != null) {
            homeMonitorLiveData?.userEmail!!.postValue(currentuser?.email)
            ntFireBaseRealtimeData?.connectFor(this, currentuser?.email)

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    Log.d(TAG, token)
                    Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                    homeMonitorLiveData?.deviceToken?.postValue(token)
                })
        }
        else {
           homeMonitorLiveData?.userEmail?.postValue("")
            ntFireBaseRealtimeData?.disconnect()
        }

    }

    fun setupFakeNotifications(){
        var t = object : CountDownTimer(Long.MAX_VALUE, 5000) {
            // This is called every interval. (Every 10 seconds in this example)
            override fun onTick(millisUntilFinished: Long) {

                //val randomInteger = SecureRandom().nextInt(120)// (32..120).shuffled().first()
                val randomInteger = (32..120).shuffled().first()
                homeMonitorLiveData?.temperature?.postValue(randomInteger)
                Log.d(TAGFAKENOTIFICATION, "TimerT tick "  + randomInteger)
            }

            override fun onFinish() {
                Log.d(TAGFAKENOTIFICATION,"TimerT last tick")
                start()
            }
        }.start()


        var h = object : CountDownTimer(Long.MAX_VALUE, 5000) {
            // This is called every interval. (Every 10 seconds in this example)
            override fun onTick(millisUntilFinished: Long) {

                //val randomInteger = SecureRandom().nextInt(120)// (32..120).shuffled().first()
                val randomInteger = (0..100).shuffled().first()
                homeMonitorLiveData?.humidity?.postValue(randomInteger)
                Log.d(TAGFAKENOTIFICATION, "TimerH tick "  + randomInteger)
            }

            override fun onFinish() {
                Log.d(TAGFAKENOTIFICATION,"TimerH last tick")
                start()
            }
        }.start()
    }

    companion object {
        const val TAG = "MainActivity"
        const val TAGFAKENOTIFICATION = "FakeNotification"

    }
}