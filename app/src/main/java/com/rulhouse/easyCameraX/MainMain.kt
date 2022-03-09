package com.rulhouse.easyCameraX

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.rulhouse.easyCameraX.camera.CameraXActivity
import com.rulhouse.easyCameraX.repository.ResultCodeList
import java.io.File

class MainMain {
    fun openCameraActivity(activity: ComponentActivity, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(activity, CameraXActivity::class.java)
        intent.putExtra("ResultCode", ResultCodeList.TakePicture)
        launcher.launch(intent)
    }
}