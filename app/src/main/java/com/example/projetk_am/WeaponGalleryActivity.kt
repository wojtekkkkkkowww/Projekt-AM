package com.example.projetk_am

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WeaponGalleryActivity  : AppCompatActivity() {
    private val imageList: MutableList<Bitmap> = mutableListOf()
    var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weapon_gallery)
        loadBitmaps()
        val adapter = ImagesAdapter(imageList,this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,5)
    }
    private fun loadBitmaps() {
        // Add bitmaps to the imageList here
        val cannon2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bow)
        val cannon1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)
        val cannon3: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.tank)
        val cannon4: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.human)
        val cannon5: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.bazooka)

        imageList.add(cannon2)
        imageList.add(cannon1)
        imageList.add(cannon3)
        imageList.add(cannon4)
        imageList.add(cannon5)
    }
}