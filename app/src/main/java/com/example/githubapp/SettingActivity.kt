package com.example.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.R
import com.example.githubapp.databinding.ActivityFavoriteBinding
import com.example.githubapp.databinding.ActivitySettingBinding
import com.example.githubapp.factory.ViewModelFactory
import com.example.githubapp.ui.DetailViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val switchTheme = findViewById<Switch>(R.id.switch_theme)
        val settingViewModel = obtainViewModel(this)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
        val btnBackSetting = findViewById<ImageView>(R.id.btn_settings_back)

        btnBackSetting.setOnClickListener {
            onBackPressed()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): SettingViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, null)
        return ViewModelProvider(activity, factory).get(SettingViewModel::class.java)
    }
}