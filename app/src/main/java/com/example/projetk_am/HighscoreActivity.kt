package com.example.projetk_am

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HighscoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.highscore_activity)
        val backButton : Button = findViewById(R.id.backButton)
        backButton.setOnClickListener{
             finish()
             overridePendingTransition(0,0)
        }
    }
}