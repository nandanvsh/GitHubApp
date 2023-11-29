package com.example.githubapp.ui

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.retrofit.ApiConfig
import com.example.githubapp.database.Favorite
import com.example.githubapp.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailViewModel(application: Application) : ViewModel(){
    private val _isloading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isloading

    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser: LiveData<DetailResponse> = _detailUser

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun findDetail(data: String){
        _isloading.value = true
        val client = ApiConfig.getApiService().getDetailUserAccount(data)
        client.enqueue(object : retrofit2.Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isloading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isloading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        } )
    }

    fun isFavorite(username: String): LiveData<Favorite> = mFavoriteRepository.getFavoriteByUsername(username)
    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun delete(username: String) {
        mFavoriteRepository.delete(username)
    }
}