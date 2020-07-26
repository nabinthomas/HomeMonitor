package com.aze.homemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        menu?.findItem(R.id.statusMenu)?.setVisible(true)
        menu?.findItem(R.id.settingsMenu)?.setVisible(true)
        menu?.findItem(R.id.logoutMenu)?.setVisible(false)

        return super.onPrepareOptionsMenu(menu)
    }
    fun showStatus() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.statusFragment)

        invalidateOptionsMenu()
    }

    fun showSettings() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
        invalidateOptionsMenu()
    }

    fun logout() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
        invalidateOptionsMenu()
    }
}