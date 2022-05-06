package com.example.befit.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val sex: Char,
    val birth_date: String,
    val height: Int,
    val weight: Int,
    val goal: Char,
    val activity: Char
): Parcelable
