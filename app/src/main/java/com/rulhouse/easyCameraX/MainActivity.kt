package com.rulhouse.easyCameraX

import android.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.rulhouse.easyCameraX.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var viewBinding: MainActivityBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = MainActivityBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        viewBinding.takePictureButton.setOnClickListener {
            viewModel.onEvent(MainEvent.OpenCamera(this))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 50) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val imageFileDir: File = File(data?.getStringExtra("ImageFile"))
//            Log.d("testOutput", imageFile.exists().toString())
            Log.d("testOutput", imageFileDir.absolutePath)
            if (imageFileDir.exists()) {
                Log.d("testOutput", imageFileDir.absolutePath)
                val myBitmap = BitmapFactory.decodeFile(imageFileDir.absolutePath)
                viewBinding.pictureImageView.setImageBitmap(myBitmap)
            }
        }
    }
}