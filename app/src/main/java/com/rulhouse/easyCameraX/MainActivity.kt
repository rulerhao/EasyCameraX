package com.rulhouse.easyCameraX

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.rulhouse.easyCameraX.databinding.MainActivityBinding
import com.rulhouse.easyCameraX.repository.ResultCodeList
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var viewBinding: MainActivityBinding

    private val viewModel: MainViewModel by viewModels()

    lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = MainActivityBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        // Picture Observer
        val pictureObserver = Observer<File> { value ->
            if (value.exists()) {
                val myBitmap = BitmapFactory.decodeFile(value.absolutePath)
                viewBinding.pictureImageView.setImageBitmap(myBitmap)
            }
        }
        viewModel.picture.observe(this, pictureObserver)

        cameraResultLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == ResultCodeList.TakePicture) {
                val dir: String = result.data?.extras?.getString("ImageFile")!!
                viewModel.picture.value = File(dir)
            }
        }

        // Take picture button
        viewBinding.takePictureButton.setOnClickListener {
            viewModel.onEvent(MainEvent.OpenCamera(this, cameraResultLauncher))
        }
    }
}