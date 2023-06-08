package com.example.mixmate.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.mixmate.R
import com.example.mixmate.data.ResponseData
import com.example.mixmate.databinding.ActivityResultBinding
import java.io.IOException


class ResultActivity : AppCompatActivity() {
  private lateinit var binding: ActivityResultBinding
  private var photoUri: Uri? = null
  private var apiResponse: ResponseData? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityResultBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val extras = intent.extras
    if (extras != null) {
      photoUri = extras.getParcelable("photo")
      apiResponse = extras.getSerializable("api_response") as? ResponseData
    }
    if (photoUri != null) {
      flipImageHorizontally(binding.resultUserPhoto, photoUri)
    }

    Log.d("RESULT_ACTIVITY", "API Response: $apiResponse")
  }

  private fun flipImageHorizontally(imageView: ImageView, photoUri: Uri?) {
    photoUri?.let { uri ->
      try {
        val inputStream = imageView.context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        // Flip the bitmap horizontally
        val matrix = Matrix()
        matrix.postScale(-1f, 1f)
        matrix.postRotate(90f)
        val flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)

        // Set the flipped bitmap to the ImageView
        imageView.setImageBitmap(flippedBitmap)
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
}


