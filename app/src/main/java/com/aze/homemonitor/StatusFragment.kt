package com.aze.homemonitor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aze.homemonitor.ui.login.LoginViewModel
import com.aze.homemonitor.ui.login.LoginViewModelFactory
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.*


class StatusFragment : Fragment() {
    var temperatureTextView: TextView? = null

    var homeMonitorLiveData: HomeMonitorLiveDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootview : View = inflater.inflate(R.layout.fragment_status, container, false)

        temperatureTextView = rootview.findViewById(R.id.temperatureTextView)

        activity?.let {
            homeMonitorLiveData = ViewModelProviders.of(it).get(HomeMonitorLiveDataModel::class.java)
        }

        homeMonitorLiveData!!.temperature.observe(this,
            object: Observer<Int> {
                override fun onChanged(t: Int) {
                    Log.d(MainActivity.TAG, " Fragment temperature " + t)
                    updateTemperature(t)
                }
            }
        )


        return rootview
    }

    fun updateTemperature(temperature: Int){
        temperatureTextView?.setText(Integer.toString(temperature))
    }


}