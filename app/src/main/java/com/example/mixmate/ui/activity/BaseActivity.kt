package com.example.mixmate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mixmate.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity() {
  private lateinit var bottomNavigationView: BottomNavigationView

  private val onNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.nav_check -> {
          showFragment(CheckFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_explore -> {
          showFragment(ExploreFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_cart -> {
          showFragment(CartFragment())
          return@OnNavigationItemSelectedListener true
        }
        R.id.nav_profile -> {
          showFragment(LoginFragment())
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
    showFragment(CheckFragment())
  }

  private fun showFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, fragment)
      .commit()
  }
}