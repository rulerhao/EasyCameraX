package com.rulhouse.easyCameraX.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.lifecycle.Observer
import com.rulhouse.easyCameraX.databinding.CameraxActivityBinding
import java.io.File

@AndroidEntryPoint
class CameraXActivity : ComponentActivity() {
    private lateinit var viewBinding: CameraxActivityBinding

    private lateinit var cameraExecutor: ExecutorService

    private val viewModel: CameraXViewModel by viewModels()

    private var takePictureResultCode: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        takePictureResultCode = this.intent.extras?.getInt("ResultCode")

        viewBinding = CameraxActivityBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        // Request camera permissions
        if (CameraPermission.allPermissionsGranted(this)) {
            viewModel.onEvent(CameraEvent.StartCamera(this, viewBinding))
        } else {
            launcher.launch(
                CameraPermission.REQUIRED_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        val zoomRatioObserver = Observer<Float> { value ->
            viewModel.onEvent(CameraEvent.SetZoomRatio(value))
        }
        viewModel.zoomRatio.observe(this, zoomRatioObserver)

        // When this dir changes, means that we finish capture picture so that return it to
        // MainActivity
//        val takePhotoDirObserver = Observer<String> { value ->
//            val intent = Intent()
//            intent.putExtra("ImageFileName", value)
//            setResult(ComponentActivity.RESULT_OK, intent)
//            finish()
//        }
//        viewModel.takePhotoDir.observe(this, takePhotoDirObserver)

        val imageFileObserver = Observer<String> { value ->
            takePictureResultCode?.let {
                CameraEvent.ReturnPictureFile(this, value, it)
            }?.let { viewModel.onEvent(it) }
        }
        viewModel.pictureDir.observe(this, imageFileObserver)

        viewBinding.imageCaptureButton.setOnClickListener {
            viewModel.onEvent(CameraEvent.TakePhoto(this))
        }
        viewBinding.viewFinder.setOnTouchListener(zoomTouchListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    val zoomTouchListener = View.OnTouchListener { _, event ->
        viewModel.onEvent(CameraEvent.ZoomGesture(event))

        true
    }

    override fun onDestroy() {
        super.onDestroy()

        cameraExecutor.shutdown()
    }

    private var launcher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach { permission ->
                if (CameraPermission.allPermissionsGranted(this)) {
                    viewModel.onEvent(CameraEvent.StartCamera(this, viewBinding))
                } else {
                    when(permission.key) {
                        Manifest.permission.CAMERA -> {
                            CameraPermission.goToSettings(this)
                            Toast.makeText(this,"${permission.key} Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    finish()
                }
            }
        }
}