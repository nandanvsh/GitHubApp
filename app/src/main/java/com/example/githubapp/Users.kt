package com.example.githubapp
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val name: String,
    val photo: Int
) : Parcelable
