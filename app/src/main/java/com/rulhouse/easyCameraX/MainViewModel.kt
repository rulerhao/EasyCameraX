package com.rulhouse.easyCameraX

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    private val mainMain = MainMain()

    val picture: MutableLiveData<File> by lazy {
        MutableLiveData<File>()
    }

    fun onEvent(event: MainEvent) {
        when(event) {
            is MainEvent.OpenCamera -> {
                mainMain.openCameraActivity(event.activity, event.launcher)
            }
        }
    }
}