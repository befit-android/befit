package com.example.befit.viewmodel.auth

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.befit.model.User
import com.example.befit.model.UserInfo
import com.example.befit.repository.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(
    private val sharedPreferences: SharedPreferences,
    private val apiRepository: ApiRepository
) : ViewModel() {

    val myResponse: MutableLiveData<Response<User>> = MutableLiveData()
    val myResponseUserInfo: MutableLiveData<Response<UserInfo>> = MutableLiveData()
    val myResponseString: MutableLiveData<Response<String>> = MutableLiveData()
    private var userToken: String? = null

    // auth methods

    fun getUser(): Boolean {
        val token = sharedPreferences.getString("token", null)
        return if (token != null || userToken != null) {
            viewModelScope.launch {
                if (token != null) {
                    val response = apiRepository.getUser("Token $token")
                    myResponse.value = response
                } else {
                    val response = apiRepository.getUser("Token $userToken")
                    myResponse.value = response
                }
            }
            true
        } else
            false
    }

    fun register(
        email: String,
        first_name: String,
        sex: Char,
        date_of_birth: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = apiRepository.register(email, first_name, sex, date_of_birth, password)
            userToken = response.body()?.token.toString()
            sharedPreferences.edit().putString("token", userToken).apply()
            myResponseUserInfo.value = response
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = apiRepository.login(email, password)
            userToken = response.body()?.token.toString()
            sharedPreferences.edit().putString("token", userToken).apply()
            myResponseUserInfo.value = response
        }
    }

    fun logout() {
        val token = sharedPreferences.getString("token", null)
        viewModelScope.launch {
            val response = apiRepository.logout("Token $token")
            myResponseString.value = response
            userToken = null
            sharedPreferences.edit().putString("token", userToken).apply()
        }
    }
}