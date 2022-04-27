package com.example.befit.repository

import com.example.befit.api.RetrofitInstance
import com.example.befit.model.User
import com.example.befit.model.UserInfo
import retrofit2.Response

class ApiRepository {

    // auth methods

    suspend fun getUser(token: String): Response<User> {
        return RetrofitInstance.api.getUser(token)
    }

    suspend fun register(
        email: String,
        first_name: String,
        sex: Char,
        date_of_birth: String,
        password: String
    ): Response<UserInfo> {
        return RetrofitInstance.api.register(email, first_name, sex, date_of_birth, password)
    }

    suspend fun login(email: String, password: String): Response<UserInfo> {
        return RetrofitInstance.api.login(email, password)
    }

    suspend fun logout(token: String): Response<String> {
        return RetrofitInstance.api.logout(token)
    }
}