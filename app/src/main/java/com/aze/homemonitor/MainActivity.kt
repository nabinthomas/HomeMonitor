package com.aze.homemonitor

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser



class MainActivity : AppCompatActivity() {

    private var currentuser: FirebaseUser? = null

    private var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    public var statusFragment: StatusFragment? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentuser = null
        setContentView(R.layout.activity_main)
        setupFakeNotifications()


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
        }
        else
        {
            menu?.findItem(R.id.statusMenu)?.setVisible(false)
            menu?.findItem(R.id.settingsMenu)?.setVisible(false)
            menu?.findItem(R.id.logoutMenu)?.setVisible(false)
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

            homeMonitorLiveData!!.temperature.observe(this,
                object: Observer<Int> {
                    override fun onChanged(t: Int) {
                        Log.d(TAG, "temperature " + t)
                        statusFragment?.updateTemperature(t)
                    }
                }
            )
        }
        else {
            homeMonitorLiveData?.temperature?.removeObservers(this)

        }

    }

    fun setupFakeNotifications(){
        var t = object : CountDownTimer(Long.MAX_VALUE, 5000) {
            // This is called every interval. (Every 10 seconds in this example)
            override fun onTick(millisUntilFinished: Long) {

                //val randomInteger = SecureRandom().nextInt(120)// (32..120).shuffled().first()
                val randomInteger = (32..120).shuffled().first()
                homeMonitorLiveData?.temperature?.postValue(randomInteger)
                Log.d(TAGFAKENOTIFICATION, "Timer tick "  + randomInteger)
            }

            override fun onFinish() {
                Log.d(TAGFAKENOTIFICATION,"Timer last tick")
                start()
            }
        }.start()
    }

    companion object {
        const val TAG = "MainActivity"
        const val TAGFAKENOTIFICATION = "FakeNotification"

    }
}