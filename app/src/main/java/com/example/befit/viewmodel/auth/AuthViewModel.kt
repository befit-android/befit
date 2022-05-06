package com.example.befit.viewmodel.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.befit.model.User
import com.example.befit.repository.ApiRepository
import com.example.befit.util.Utility.hideProgressBar
import com.example.befit.util.Utility.isInternetAvailable
import com.example.befit.util.Utility.showProgressBar
import kotlinx.coroutines.launch

class AuthViewModel(
    private val sharedPreferences: SharedPreferences,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val responseUser: MutableLiveData<User> = MutableLiveData()
    private val responseUserInfo: MutableLiveData<String> = MutableLiveData()
    private val responseLogout: MutableLiveData<String> = MutableLiveData()

    private val errorUser: MutableLiveData<String> = MutableLiveData()
    private val errorUserInfo: MutableLiveData<String> = MutableLiveData()
    private val errorLogout: MutableLiveData<String> = MutableLiveData()

    private var isLoginProcess = false

    fun getResponseUser(): LiveData<User> = responseUser
    fun getResponseUserInfo(): LiveData<String> = responseUserInfo
    fun getResponseLogout(): LiveData<String> = responseLogout

    fun getErrorUser(): LiveData<String> = errorUser
    fun getErrorUserInfo(): LiveData<String> = errorUserInfo
    fun getErrorLogout(): LiveData<String> = errorLogout

    fun register(
        context: Context,
        email: String,
        first_name: String,
        sex: Char,
        birth_date: String,
        height: Int,
        weight: Int,
        goal: Char,
        activity: Char,
        password: String
    ) {
        errorUserInfo.value = ""
        responseUserInfo.value = ""
        if (context.isInternetAvailable()) {
            viewModelScope.launch {
                val response =
                    apiRepository.register(
                        email,
                        first_name,
                        sex,
                        birth_date,
                        height,
                        weight,
                        goal,
                        activity,
                        password
                    )
                if (response.isSuccessful) {
                    sharedPreferences.edit().putString("token", response.body()?.token).apply()
                    responseUserInfo.value = response.message()
                } else
                    errorUserInfo.value =
                        response.errorBody()!!.string().substringAfter("[\"").dropLast(3)
            }
        }
    }

    fun login(context: Context, email: String, password: String) {
        errorUserInfo.value = ""
        responseUserInfo.value = ""
        if (context.isInternetAvailable()) {
            viewModelScope.launch {
                val response = apiRepository.login(email, password)
                if (response.isSuccessful) {
                    sharedPreferences.edit().putString("token", response.body()?.token).apply()
                    responseUserInfo.value = response.message()
                } else {
                    errorUserInfo.value =
                        response.errorBody()!!.string().substringAfter("[\"").dropLast(3)
                }
            }
        }
    }

    fun logout(context: Context) {
        errorLogout.value = ""
        val token = sharedPreferences.getString("token", null)
        if (context.isInternetAvailable()) {
            viewModelScope.launch {
                val response = apiRepository.logout("Token $token")
                if (response.isSuccessful) {
                    sharedPreferences.edit().putString("token", null).apply()
                    responseLogout.value = response.message()
                } else
                    errorLogout.value =
                        response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
            }
        }
    }

    fun getUser(context: Context) {
        errorUser.value = ""
        if (context.isInternetAvailable()) {
            viewModelScope.launch {
                context.showProgressBar()
                val token = sharedPreferences.getString("token", null)
                val response = apiRepository.getUser("Token $token")
                hideProgressBar()
                if (response.isSuccessful)
                    responseUser.value = response.body()
                else
                    errorUser.value = response.message()
            }
        }
    }
}