package com.example.githubapp.ui

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubapp.SettingPreferences
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.data.response.UsersResponse
import com.example.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _listUsername = MutableLiveData<List<ItemsItem?>>()
    val listReview: LiveData<List<ItemsItem?>> = _listUsername

    private val _isloading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isloading

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun findUsername(data: String) {
        _isloading.value = true
        val client = ApiConfig.getApiService().getListUsers(data)
        client.enqueue(object : retrofit2.Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                _isloading.value = false
                if (response.isSuccessful) {
                    _listUsername.value = response.body()?.items!!
                } else {
                    Log.e(ContentValues.TAG, "onFilure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                _isloading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

}