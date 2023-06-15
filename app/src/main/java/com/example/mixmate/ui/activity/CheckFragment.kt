package com.example.mixmate.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mixmate.api.ApiManager
import com.example.mixmate.api.ApiService
import com.example.mixmate.data.ResponseData
import com.example.mixmate.databinding.FragmentCheckBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CheckFragment : Fragment() {

  private var _binding: FragmentCheckBinding? = null
  private val binding get() = _binding!!
  private var cameraProvider: ProcessCameraProvider? = null
  private var camera: Camera? = null
  private var imageCapture: ImageCapture? = null
  private var currentPhotoUri: Uri? = null
  private var currentPhotoFile: File? = null

  private lateinit var apiService: ApiService

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCheckBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupViews()
    requestCameraPermission()
  }


  override fun onResume() {
    super.onResume()
    startCamera()
  }

  override fun onPause() {
    super.onPause()
    releaseCamera()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupViews() {
    binding.buttonSubmit.setOnClickListener {
      val photoFile = currentPhotoFile ?: return@setOnClickListener
      binding.loadingView.visibility = View.VISIBLE
      sendImage(photoFile)
    }

    binding.buttonTakePicture.setOnClickListener {
      capturePhoto()
    }

    binding.buttonDelete.setOnClickListener {
      deletePhoto()
    }
  }

  private fun sendImage(imageFile: File) {
    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
    val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

    val okHttpClient = OkHttpClient.Builder()
      .connectTimeout(10, TimeUnit.MINUTES) // Set timeout to 10 minutes
      .readTimeout(10, TimeUnit.MINUTES)
      .writeTimeout(10, TimeUnit.MINUTES)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    apiService = retrofit.create(ApiService::class.java)

    val call = apiService.uploadImage(multipartBody)
    call.enqueue(object : Callback<ResponseData> {
      override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
        binding.loadingView.visibility = View.GONE
        if (response.isSuccessful) {
          val apiResponse = response.body()
          Log.d(TAG, "API Response: $apiResponse")
          apiResponse?.let {
            startResultActivity(currentPhotoUri!!, it)
          }
        } else {
          // Handle error response
          val errorMessage = response.message()
          Log.e(TAG, "API Error: $errorMessage")
          Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<ResponseData>, t: Throwable) {
        binding.loadingView.visibility = View.GONE
        Log.e(TAG, "API Call Failed: ${t.message}")
        Toast.makeText(requireContext(), "API Call Failed: ${t.message}", Toast.LENGTH_SHORT).show()
      }
    })
  }


  private fun startResultActivity(photo: Uri, apiResponse: ResponseData) {
    val intent = Intent(requireContext(), ResultActivity::class.java)
    intent.putExtra("photo", photo)
    intent.putExtra("api_response", apiResponse)
    startActivity(intent)
  }




  private fun requestCameraPermission() {
    if (ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      startCamera()
    } else {
      ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(Manifest.permission.CAMERA),
        REQUEST_CAMERA_PERMISSION
      )
    }
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
    cameraProviderFuture.addListener({
      cameraProvider = cameraProviderFuture.get()
      bindCameraUseCases()
    }, ContextCompat.getMainExecutor(requireContext()))
  }

  private fun bindCameraUseCases() {
    val cameraSelector = CameraSelector.Builder()
      .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
      .build()

    val preview = Preview.Builder().build().also {
      it.setSurfaceProvider(binding.previewView.surfaceProvider)
    }

    imageCapture = ImageCapture.Builder().build()
    imageCapture?.targetRotation = Surface.ROTATION_0

    try {
      cameraProvider?.unbindAll()

      camera = cameraProvider?.bindToLifecycle(
        this,
        cameraSelector,
        preview,
        imageCapture
      )
    } catch (e: Exception) {
      Log.e(TAG, "Failed to bind camera use cases: ${e.message}")
    }
  }

  private fun releaseCamera() {
    cameraProvider?.unbindAll()
    cameraProvider = null
    camera = null
    imageCapture = null
  }

  private fun capturePhoto() {
    val outputFile = createOutputFile()
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

    imageCapture?.takePicture(
      outputOptions,
      ContextCompat.getMainExecutor(requireContext()),
      object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
          val savedUri = outputFileResults.savedUri ?: Uri.fromFile(outputFile)
          currentPhotoUri = savedUri
          currentPhotoFile = File(savedUri.path)
          displayCapturedPhoto(savedUri)
        }

        override fun onError(exception: ImageCaptureException) {
          Log.e(TAG, "Error capturing photo: ${exception.message}")
        }
      }
    )
  }

  private fun createOutputFile(): File {
    val timeStamp =
      SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir =
      requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
  }

  private fun displayCapturedPhoto(photoUri: Uri) {
    binding.previewView.visibility = View.GONE
    binding.imageView.visibility = View.VISIBLE

    val bitmap =
      MediaStore.Images.Media.getBitmap(requireContext().contentResolver, photoUri)

    val matrix = Matrix()
    matrix.postScale(-1f, 1f)
    matrix.postRotate(90f)

    val flippedBitmap = Bitmap.createBitmap(
      bitmap,
      0,
      0,
      bitmap.width,
      bitmap.height,
      matrix,
      true
    )

    binding.imageView.setImageBitmap(flippedBitmap)

    binding.btnCheckContainer.visibility = View.VISIBLE
    binding.buttonTakePicture.visibility = View.GONE
  }

  private fun deletePhoto() {
    currentPhotoUri?.let { photoUri ->
      val file = File(photoUri.path)
      if (file.exists()) {
        file.delete()
        currentPhotoFile?.delete() // Hapus juga file lokalnya
      }
      currentPhotoUri = null
      currentPhotoFile = null
      binding.imageView.setImageDrawable(null)
      binding.imageView.visibility = View.GONE
      binding.previewView.visibility = View.VISIBLE

      binding.btnCheckContainer.visibility = View.GONE
      binding.buttonTakePicture.visibility = View.VISIBLE
    }
  }

  companion object {
    private const val TAG = "CheckFragment"
    private const val REQUEST_CAMERA_PERMISSION = 123
    private const val BASE_URL = "https://predict-dn2kyiya7a-et.a.run.app/"
  }
}






