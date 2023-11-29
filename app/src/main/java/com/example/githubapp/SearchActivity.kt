package com.example.githubapp

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.databinding.ActivitySearchMenuBinding
import com.example.githubapp.factory.ViewModelFactory
import com.example.githubapp.ui.DetailViewModel
import com.example.githubapp.ui.ListUsersAdapter
import com.example.githubapp.ui.MainViewModel
import com.example.githubapp.ui.main.FavoriteActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMenuBinding
    private lateinit var listUsersAdapter: ListUsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsername.layoutManager = layoutManager

        val itemDecoration =DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsername.addItemDecoration(itemDecoration)

        listUsersAdapter = ListUsersAdapter()
        binding.rvUsername.adapter = listUsersAdapter

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = obtainViewModel(this, pref)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }
        with(binding){

            svSearch.inflateMenu(R.menu.menu_top_option)
            val srcMenu = svSearch.menu
            val favoriteMenuItem = srcMenu.findItem(R.id.item_love)
            val settingMenuItem = srcMenu.findItem(R.id.item_settings)
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
                favoriteMenuItem.setIcon(R.drawable.ic_favorite_fill_white)
                settingMenuItem.setIcon(R.drawable.ic_setting_white)
            }else{
                favoriteMenuItem.setIcon(R.drawable.ic_favorite_fill)
                settingMenuItem.setIcon(R.drawable.ic_settings)
            }

            favoriteMenuItem.setOnMenuItemClickListener {
                val intent = Intent(this@SearchActivity, FavoriteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                true
            }
            settingMenuItem.setOnMenuItemClickListener {
                val intent = Intent(this@SearchActivity, SettingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                true
            }

            svSearchView.setupWithSearchBar(svSearch)
            svSearchView
                .editText
                .setOnClickListener{
                showLoading(true)
                svSearch.text = svSearchView.text

                svSearchView.hide()

                mainViewModel.findUsername(svSearchView.text.toString())
            }

        }

        mainViewModel.findUsername("nanda")

        mainViewModel.listReview.observe(this){
                username -> setUsernameData(username)
        }

        mainViewModel.isLoading.observe(this){showLoading(it)
        }


    }

    private fun setUsernameData(usernameData:List<ItemsItem?>){
        val adapter = ListUsersAdapter()
        adapter.submitList(usernameData)
        binding.rvUsername.adapter=adapter
    }
    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity, pref: SettingPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }
}