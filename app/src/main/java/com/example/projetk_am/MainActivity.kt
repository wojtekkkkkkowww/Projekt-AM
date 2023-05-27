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
    private lateinit var launcherG: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        loadBitmaps()
        imageView.setImageBitmap(imageList[0])

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                    //
            }
        }

        launcherG = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val index = data?.getIntExtra( "index",0)
                if(index != null) {
                    currentImageIndex = index
                    imageView.setImageBitmap(imageList[index])

                }
            }
        }

    }
    private fun loadBitmaps() {
        // Add bitmaps to the imageList here
        val cannon1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)
        val cannon2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bow)
        val cannon3: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.tank)
        val cannon4: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.human)

        imageList.add(cannon2)
        imageList.add(cannon1)
        imageList.add(cannon3)
        imageList.add(cannon4)


        val bullet1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bullet)
        val bullet2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)
        val bullet3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire)
        val bullet4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bullet2)


        imageListB.add(bullet2)
        imageListB.add(bullet1)
        imageListB.add(bullet3)
        imageListB.add(bullet4)

    }

    fun imageClick(view: View) {
       // currentImageIndex = (currentImageIndex + 1) % imageList.size
       // imageView.setImageBitmap(imageList[currentImageIndex])

        val intent = Intent(this, WeaponGalleryActivity::class.java)
        launcherG.launch(intent)

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