package com.aze.homemonitor.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.aze.homemonitor.data.LoginRepository
import com.aze.homemonitor.data.Result
import androidx.lifecycle.map


import com.aze.homemonitor.R
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        }else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
}