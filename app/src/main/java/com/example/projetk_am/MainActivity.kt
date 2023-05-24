package com.example.projetk_am

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val imageList: MutableList<Bitmap> = mutableListOf()
    private var currentImageIndex: Int = 0
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        loadBitmaps()
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                    //
            }
        }


    }
    private fun loadBitmaps() {
        // Add bitmaps to the imageList here
        val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)
        val bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bow)


        imageList.add(bitmap1)
        imageList.add(bitmap2)
    }

    fun imageClick(view: View) {
        currentImageIndex = (currentImageIndex + 1) % imageList.size
        imageView.setImageBitmap(imageList[currentImageIndex])

    }
    fun buttonClick(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        launcher.launch(intent)
    }


}