package com.example.projetk_am

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private val imageList: MutableList<Bitmap> = mutableListOf()
    private val imageListB: MutableList<Bitmap> = mutableListOf()
    private val imageListC: MutableList<Bitmap> = mutableListOf()

    private lateinit var lvlbutton: Button
    private var currentImageIndex: Int = 0
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var launcherG: ActivityResultLauncher<Intent>
    private var difficulty: Int = 1
    private var bulletSpeed: Int = 2
    private var bulletSize: Int = 0

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)
        lvlbutton = findViewById(R.id.button)
        loadBitmaps()
        imageView.setImageBitmap(imageList[0])

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val score = result.data?.getIntExtra("index", 0)

                Log.i("score", "$score")

                // tu ma wyskakiwac event z nickiem i ustawiany nick

                val dialogView = layoutInflater.inflate(R.layout.dialog_nickname, null)
                val editText = dialogView.findViewById<EditText>(R.id.editText)

                AlertDialog.Builder(this)
                    .setTitle("Podaj swój nick")
                    .setView(dialogView)
                    .setPositiveButton("Kontynuuj") { dialog, which ->
                        val fileName = "scores.txt"
                        val nick = editText.text.toString().replace(" ", "")

                        try {
                            val file = File(filesDir, fileName) // Create a File object using the file path in internal storage
                            FileWriter(file, true).use { fileWriter ->
                                BufferedWriter(fileWriter).use { bufferedWriter ->
                                    bufferedWriter.write("$nick $score")
                                    bufferedWriter.newLine()
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        Log.i("score", "$score")
                    }
                    .create()
                    .show()


            }
            Log.i("score", "null")
        }

        launcherG = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val index = data?.getIntExtra( "index",0)
                if(index != null) {
                    currentImageIndex = index
                    imageView.setImageBitmap(imageList[index])
                    if(currentImageIndex == 0) {
                        imageView2.setImageBitmap(imageListC[2])
                        imageView3.setImageBitmap(imageListC[0])
                        bulletSpeed = 2
                        bulletSize = 0
                    }
                    else if(currentImageIndex == 1) {
                        imageView2.setImageBitmap(imageListC[2])
                        imageView3.setImageBitmap(imageListC[1])
                        bulletSpeed = 2
                        bulletSize = 1
                    }
                    else if(currentImageIndex == 2) {
                        imageView2.setImageBitmap(imageListC[0])
                        imageView3.setImageBitmap(imageListC[1])
                        bulletSpeed = 0
                        bulletSize = 1
                    }
                    else if(currentImageIndex == 3) {
                        imageView2.setImageBitmap(imageListC[1])
                        imageView3.setImageBitmap(imageListC[0])
                        bulletSpeed = 1
                        bulletSize = 0
                    }
                    else {
                        imageView2.setImageBitmap(imageListC[2])
                        imageView3.setImageBitmap(imageListC[2])
                        bulletSpeed = 2
                        bulletSize = 2
                    }
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
        val cannon5: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bazooka)

        imageList.add(cannon2)
        imageList.add(cannon1)
        imageList.add(cannon3)
        imageList.add(cannon4)
        imageList.add(cannon5)

        val bullet1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bullet)
        val bullet2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)
        val bullet3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire)
        val bullet4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bullet2)
        val bullet5: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.racket)

        imageListB.add(bullet2)
        imageListB.add(bullet1)
        imageListB.add(bullet3)
        imageListB.add(bullet4)
        imageListB.add(bullet5)

        val bar1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bars)
        val bar2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.barm)
        val bar3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.barl)

        imageListC.add(bar1)
        imageListC.add(bar2)
        imageListC.add(bar3)
        imageListC.add(bar2)
        imageListC.add(bar3)
    }

    fun imageClick(view: View) {
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
        intent.putExtra("value", difficulty.toString())
        intent.putExtra("speed", bulletSpeed.toString())
        intent.putExtra("size", bulletSize.toString())
        launcher.launch(intent)
    }

    fun buttonClick2(view: View) {
        if(difficulty == 1) {
            difficulty++
            lvlbutton.text = "Medium"
        } else if(difficulty == 2) {
            difficulty++
            lvlbutton.text = "Hard"
        } else {
            difficulty = 1
            lvlbutton.text = "Easy"
        }
    }

    fun highScoreButtonOnClick(view: View) {

        val intent = Intent(this, HighscoreActivity::class.java)
        startActivity(intent)

    }
}

