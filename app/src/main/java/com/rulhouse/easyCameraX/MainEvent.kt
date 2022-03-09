package com.rulhouse.easyCameraX

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

sealed class MainEvent {
    data class OpenCamera(val activity: ComponentActivity, val launcher: ActivityResultLauncher<Intent>): MainEvent()
}