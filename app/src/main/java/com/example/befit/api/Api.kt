package com.example.befit.api

import com.example.befit.model.User
import com.example.befit.model.UserInfo
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // auth methods

    @GET("auth/user/")
    suspend fun getUser(@Header("Authorization") token: String): Response<User>

    @FormUrlEncoded
    @POST("auth/register/")
    suspend fun register(
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("sex") sex: Char,
        @Field("birth_date") birth_date: String,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
        @Field("goal") goal: Char,
        @Field("activity") activity: Char,
        @Field("password") password: String
    ): Response<UserInfo>

    @FormUrlEncoded
    @POST("auth/login/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserInfo>

    @POST("auth/logout/")
    suspend fun logout(@Header("Authorization") token: String): Response<String>


}