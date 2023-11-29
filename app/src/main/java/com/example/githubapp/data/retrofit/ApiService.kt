package com.example.githubapp.data.retrofit

import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.response.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUsers(
        @Query("q") q:String
    ): Call<UsersResponse>

    @GET("users/{username}")
    fun getDetailUserAccount(
        @Path("username") username:String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username:String
    ): Call<List<DetailResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username:String
    ): Call<List<DetailResponse>>
}