package com.example.mixmate.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mixmate.R
import com.example.mixmate.databinding.ActivityBaseBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity() {
  private lateinit var bottomNavigationView: BottomNavigationView

  private val onNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.nav_home -> {
          showFragment(HomeFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_camera -> {
          showFragment(CameraFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_recommendation -> {
          showFragment(RecommendationFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_profile -> {
          showFragment(ProfileFragment())
          return@OnNavigationItemSelectedListener true
        }
      }
      false
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_base)

    bottomNavigationView = findViewById(R.id.bottomNavigationView)
    bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    // Set default fragment
    showFragment(HomeFragment())
  }

  private fun showFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, fragment)
      .commit()
  }
}