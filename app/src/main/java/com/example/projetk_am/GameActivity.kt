package com.example.projetk_am

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle? = intent.extras
        val cannonPath = bundle?.getString("cannonPath")
        val bulletPath = bundle?.getString("bulletPath")
        val weapon = BitmapFactory.decodeFile(cannonPath)
        val bullet = BitmapFactory.decodeFile(bulletPath)
        var difficulty = bundle!!.getString("value")
        var speed = bundle!!.getString("speed")
        var size = bundle!!.getString("size")
        val mView = GameView(this)
        mView.weapon = weapon
        mView.bulletImage = bullet
        mView.gameActivity = this

        if (difficulty != null) {
            mView.difficulty = difficulty.toInt()
        }
        setContentView(mView)

    }
}