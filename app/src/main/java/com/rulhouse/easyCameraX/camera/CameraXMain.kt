package com.rulhouse.easyCameraX.camera

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.rulhouse.easyCameraX.databinding.CameraxActivityBinding
import com.rulhouse.easyCameraX.repository.ResultCodeList
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraXMain {
    private var imageCapture: ImageCapture? = null

    private lateinit var camera: Camera

    private val zoomGestureMethods = CameraZoomGestureEvent()

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmssSSS"
    }

    fun gestureEvent(event: CameraEvent) {
        zoomGestureMethods.gestureEvent(event)
    }

    fun zoomEvent(event: CameraEvent): Float {
        return zoomGestureMethods.zoomEvent(event, camera)
    }

    fun setZoomRatio(ratio: Float) {
        camera.cameraControl.setZoomRatio(ratio)
    }

    fun startCamera(activity: ComponentActivity, viewBinding: CameraxActivityBinding) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    activity, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    fun takePhoto(activity: ComponentActivity, viewModel: CameraXViewModel) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val dir = File(activity.getExternalFilesDir(null).toString() + "/Pictures/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val type = "jpeg"
        val file = File("${dir.absoluteFile}/$name.$type")

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded uri: ${output.savedUri}"
                    Log.d(TAG, msg)

                    viewModel.pictureDir.value = file.absolutePath
                }
            }
        )
    }

    fun returnPictureFile(activity: ComponentActivity, pictureFileDir: String, resultCode: Int) {
        val intent = Intent()
        intent.putExtra("ImageFile", pictureFileDir)
        activity.setResult(resultCode, intent)
        activity.finish()
    }
}