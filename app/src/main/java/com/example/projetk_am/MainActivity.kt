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
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val imageList: MutableList<Bitmap> = mutableListOf()
    private val imageListB: MutableList<Bitmap> = mutableListOf()

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
        val cannon1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)
        val cannon2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bow)

        imageList.add(cannon1)
        imageList.add(cannon2)

        val bullet1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bullet)
        val bullet2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)

        imageListB.add(bullet1)
        imageListB.add(bullet2)




    }

    fun imageClick(view: View) {
        currentImageIndex = (currentImageIndex + 1) % imageList.size
        imageView.setImageBitmap(imageList[currentImageIndex])


    }
    fun buttonClick(view: View) {
        val file1 = File(this.cacheDir, "cannon.png") // create a file to save the bitmap as PNG
        val outputStream1 = FileOutputStream(file1)
        imageList[currentImageIndex].compress(Bitmap.CompressFormat.PNG, 100, outputStream1) // save the bitmap as PNG to the file
        outputStream1.close()

        val file2 = File(this.cacheDir, "bullet.png") // create a file to save the bitmap as PNG
        val outputStream2 = FileOutputStream(file2)
        imageListB[currentImageIndex].compress(Bitmap.CompressFormat.PNG, 100, outputStream2) // save the bitmap as PNG to the file
        outputStream2.close()


        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("cannonPath", file1.absolutePath) // pass the file path as an extra
        intent.putExtra("bulletPath", file2.absolutePath) // pass the file path as an extra
        launcher.launch(intent)
    }


}