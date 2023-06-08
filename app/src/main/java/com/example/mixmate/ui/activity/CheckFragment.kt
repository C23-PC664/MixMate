package com.example.mixmate.ui.activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mixmate.databinding.FragmentCheckBinding

class CheckFragment : Fragment() {

  private var _binding: FragmentCheckBinding? = null
  private val binding get() = _binding!!

  private var cameraProvider: ProcessCameraProvider? = null
  private var camera: Camera? = null

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
    binding.buttonSubmit.setOnClickListener {
      // Handle submit button click
    }
  }

  override fun onResume() {
    super.onResume()
    requestCameraPermission()
  }

  override fun onPause() {
    super.onPause()
    releaseCamera()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
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

    try {
      cameraProvider?.unbindAll()

      camera = cameraProvider?.bindToLifecycle(
        this,
        cameraSelector,
        preview
      )
    } catch (e: Exception) {
      Log.e(TAG, "Failed to bind camera use cases: ${e.message}")
    }
  }

  private fun releaseCamera() {
    cameraProvider?.unbindAll()
    cameraProvider = null
    camera = null
  }

  companion object {
    private const val TAG = "CameraFragment"
    private const val REQUEST_CAMERA_PERMISSION = 123
  }
}


