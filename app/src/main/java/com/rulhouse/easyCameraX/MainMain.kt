package com.rulhouse.easyCameraX

import android.content.Intent
import androidx.activity.ComponentActivity
import com.rulhouse.easyCameraX.camera.CameraXActivity

class MainMain {
    fun openCameraActivity(activity: ComponentActivity) {
        val intent = Intent(activity, CameraXActivity::class.java)
        activity.startActivityForResult(intent, 50)
    }
}