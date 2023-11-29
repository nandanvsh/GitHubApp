package com.example.githubapp.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.FollowersFragment
import com.example.githubapp.R
import com.example.githubapp.SearchActivity
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.database.Favorite
import com.example.githubapp.databinding.ActivityDetailUserBinding
import com.example.githubapp.factory.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private var isFavorite: Boolean = false

    private lateinit var data: ItemsItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getUsername = intent.getStringExtra(EXTRA_TITLE)

        val dataJson = if (Build.VERSION.SDK_INT >= 33) {
            intent.getStringExtra(DETAIL_KEY)
        } else {
            @Suppress("DEPRECATION")
            intent.getStringExtra(DETAIL_KEY)
        }

        val detailViewModel = obtainViewModel(this)

        data = Gson().fromJson(dataJson, ItemsItem::class.java)
        detailViewModel.findDetail(data.login!!)
        detailViewModel.detailUser.observe(this){binding.realName.text=it.name}
        binding.username.text = data.login

        binding.bckBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }



        val arg = Bundle()
        arg.putString(FollowersFragment.ARG_USERNAME, data.login)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, arg)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }
        detailViewModel.detailUser.observe(this){
            setUsernameData(it)
        }

        detailViewModel.isFavorite(data.login!!).observe(this){
            isFavorite = it != null
            changeButtonFav(isFavorite)
        }


        binding?.floatingLove?.setOnClickListener{
            when(isFavorite){
                true -> {
                    detailViewModel.delete(data.login!!)
                    binding?.floatingLove?.setImageResource(R.drawable.ic_favorite_border)
                }
                else -> {
                    val favorite = Favorite()
                    favorite.username = data.login
                    favorite.avatar = data.avatarUrl
                    detailViewModel.insert(favorite)
                    binding?.floatingLove?.setImageResource(R.drawable.ic_favorite_fill)
                }
            }
        }

    }

    private fun setUsernameData(detailUserData: DetailResponse) {
        binding.apply {
            username.text = "@${detailUserData.login}"
            realName.text = detailUserData.name
            countFollowers.text = detailUserData.followers.toString()
            countFollowing.text = detailUserData.following.toString()
        }
        Glide.with(binding.root)
            .load(detailUserData.avatarUrl)
            .into(binding.imageView2)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
            countFollowers.visibility = if (isLoading) View.GONE else View.VISIBLE
            countFollowing.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, null)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun changeButtonFav(isFavorite: Boolean){
        when(isFavorite){
            true -> binding?.floatingLove?.setImageResource(R.drawable.ic_favorite_fill)
            else -> binding?.floatingLove?.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_TITLE = "extra_title"
        const val DETAIL_KEY = "Detail data"
    }

}