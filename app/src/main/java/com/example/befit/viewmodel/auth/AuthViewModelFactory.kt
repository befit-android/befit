package com.example.befit.viewmodel.auth

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.befit.repository.ApiRepository

class AuthViewModelFactory(
    private val sharedPreferences: SharedPreferences,
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(sharedPreferences, apiRepository) as T
    }
}