package com.example.projetk_am

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    lateinit var healthTextView: TextView
    lateinit var scoreTextView: TextView
    lateinit var ammoTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
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
        val frame = findViewById<FrameLayout>(R.id.gameContainer)
        healthTextView = findViewById<TextView>(R.id.textView5)
        scoreTextView = findViewById<TextView>(R.id.textView6)
        ammoTextView = findViewById<TextView>(R.id.textView7)


        frame.addView(mView)
        if (difficulty != null) {
            mView.difficulty = difficulty.toInt()
        }



    }
}