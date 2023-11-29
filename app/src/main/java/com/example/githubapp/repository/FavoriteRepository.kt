package com.example.githubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.database.Favorite
import com.example.githubapp.database.FavoriteDao
import com.example.githubapp.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites():  LiveData<List<Favorite>> = mFavoriteDao.getAllFavorites()

    fun getFavoriteByUsername(username: String): LiveData<Favorite> = mFavoriteDao.getFavoriteByUsername(username)

    fun insert(favorite: Favorite) {
        executorService.execute{ mFavoriteDao.insert(favorite)}
    }
    fun delete(username: String){
        executorService.execute{ mFavoriteDao.delete(username)}
    }

}