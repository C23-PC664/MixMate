package com.example.mixmate.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.mixmate.R

class LauncherActivity : AppCompatActivity() {

  private val SPLASH_DELAY: Long = 2000

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_launcher)

    Handler().postDelayed({
      val intent = Intent(this, BaseActivity::class.java)
      startActivity(intent)
      finish()
    }, SPLASH_DELAY)
  }
}
