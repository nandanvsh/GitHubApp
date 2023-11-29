package com.example.githubapp.ui

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class FollowersViewModel() : ViewModel(){
    private val _listFollowers = MutableLiveData<List<DetailResponse>>()
    val listFollowers: LiveData<List<DetailResponse>> = _listFollowers
    private val _listFollowing = MutableLiveData<List<DetailResponse>>()
    val listFollowing: LiveData<List<DetailResponse>> = _listFollowing

    private val _isloading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isloading

    fun findFollowing(username: String) {
        _isloading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : retrofit2.Callback<List<DetailResponse>>{
            override fun onResponse(
                call: Call<List<DetailResponse>>,
                response: Response<List<DetailResponse>>
            ) {
                _isloading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DetailResponse>>, t: Throwable) {
                _isloading.value = false

            }
        })
    }

    fun findFollowers(data: String){
        _isloading.value = true
        val client = ApiConfig.getApiService().getFollowers(data)
        client.enqueue(object : retrofit2.Callback<List<DetailResponse>>{
            override fun onResponse(
                call: Call<List<DetailResponse>>,
                response: Response<List<DetailResponse>>
            ) {
                _isloading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                }else{
                    Log.e(ContentValues.TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DetailResponse>>, t: Throwable) {
                _isloading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

}


