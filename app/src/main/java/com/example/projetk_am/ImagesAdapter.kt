package com.example.projetk_am


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class ImagesAdapter( var imageList: MutableList<Bitmap>,val galleryActivity: WeaponGalleryActivity) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        init{
            imageView = itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.image_layout, viewGroup, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: ImageViewHolder, position: Int) {
        viewholder.imageView.setImageBitmap(imageList[position])
        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        viewholder.imageView.setBackgroundColor(color)
        viewholder.imageView.setOnClickListener {
             galleryActivity.image = imageList[position]
            galleryActivity.intent.apply {
                putExtra("index", position)
            }
            galleryActivity.setResult(Activity.RESULT_OK, galleryActivity.intent)
            galleryActivity.finish()
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}