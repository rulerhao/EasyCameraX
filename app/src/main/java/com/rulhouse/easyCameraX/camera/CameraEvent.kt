package com.rulhouse.easyCameraX.camera

import android.view.MotionEvent
import androidx.activity.ComponentActivity
import com.rulhouse.easyCameraX.databinding.CameraxActivityBinding
import java.io.File

sealed class CameraEvent {
    data class ZoomGesture(val event: MotionEvent): CameraEvent()
    data class SetZoomRatio(val ratio: Float): CameraEvent()
    data class StartCamera(val activity: ComponentActivity, val viewBinding: CameraxActivityBinding): CameraEvent()
    data class TakePhoto(val activity: ComponentActivity): CameraEvent()
    data class ReturnPictureFile(val activity: ComponentActivity, val imageFileDir: String): CameraEvent()
}
