package com.example.githubapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.databinding.ActivityFavoriteBinding
import com.example.githubapp.factory.ViewModelFactory
import com.example.githubapp.ui.FollowAdapter

class FavoriteActivity : AppCompatActivity() {

    private var _favoriteActivityBinding : ActivityFavoriteBinding? = null
    private val binding get() = _favoriteActivityBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _favoriteActivityBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this) { favoriteList ->
            adapter.setListFavorite(favoriteList)
        }


        adapter = FavoriteAdapter()
        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

        binding?.btnFavoriteBack?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel{
        val factory = ViewModelFactory.getInstance(activity.application, null)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _favoriteActivityBinding = null
    }

}