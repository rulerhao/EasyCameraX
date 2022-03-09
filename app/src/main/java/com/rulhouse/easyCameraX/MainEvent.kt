package com.rulhouse.easyCameraX

import androidx.activity.ComponentActivity

sealed class MainEvent {
    data class OpenCamera(val activity: ComponentActivity): MainEvent()
}