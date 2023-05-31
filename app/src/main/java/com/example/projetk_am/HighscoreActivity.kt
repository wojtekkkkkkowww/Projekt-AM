package com.example.projetk_am

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class HighscoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.highscore_activity)
        val backButton: Button = findViewById(R.id.backButton)
        val scrollView = findViewById<GridLayout>(R.id.scrollView)
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        val fileName = "scores.txt"

        val scoreList = mutableListOf<Pair<String, Int>>()

        try {
            val file = File(filesDir, fileName)

            FileReader(file).use { fileReader ->
                BufferedReader(fileReader).use { bufferedReader ->
                    var line: String? = bufferedReader.readLine()
                    while (line != null) {
                        val parts = line.split(" ")
                        val nick = parts[0]
                        val score = parts[1].toInt()
                        scoreList.add(nick to score)
                        line = bufferedReader.readLine()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        scoreList.sortByDescending { it.second }

        for ((nick, score) in scoreList) {
            val textView = TextView(this)
            textView.text = "Nick: $nick, Score: $score"
            scrollView.addView(textView)
        }
    }
}
