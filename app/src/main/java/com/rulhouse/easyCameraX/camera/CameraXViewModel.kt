package com.rulhouse.easyCameraX.camera

import android.view.MotionEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor(
) : ViewModel() {
    val zoomRatio: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    val pictureDir: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val cameraXMain = CameraXMain()

    fun onEvent(event: CameraEvent) {
        when(event) {
            is CameraEvent.ZoomGesture -> {
                when (event.event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_MOVE -> {
                        zoomRatio.value = cameraXMain.zoomEvent(event)
                    }
                    else -> {
                        cameraXMain.gestureEvent(event)
                    }
                }
            }
            is CameraEvent.SetZoomRatio -> {
                cameraXMain.setZoomRatio(event.ratio)
            }
            is CameraEvent.StartCamera -> {
                cameraXMain.startCamera(event.activity, event.viewBinding)
            }
            is CameraEvent.TakePhoto -> {
                cameraXMain.takePhoto(event.activity, this)
            }
            is CameraEvent.ReturnPictureFile -> {
                cameraXMain.returnPictureFile(event.activity, event.imageFileDir)
            }
        }
    }
}