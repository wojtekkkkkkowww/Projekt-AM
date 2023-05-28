package com.example.projetk_am

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ScrollView
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
        val scrollView = findViewById<GridLayout>(R.id.scrollView) // Replace with your GridLayout ID
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        val fileName = "scores.txt"

        val scoreList = mutableListOf<Pair<String, Int>>() // List to store nick and score pairs

        try {
            val file = File(filesDir, fileName) // Create a File object using the file path in internal storage

            // Open the file for reading
            FileReader(file).use { fileReader ->
                // Create a BufferedReader for efficient reading
                BufferedReader(fileReader).use { bufferedReader ->
                    var line: String? = bufferedReader.readLine()
                    while (line != null) {
                        // Split the line into nick and score
                        val parts = line.split(" ")
                        val nick = parts[0]
                        val score = parts[1].toInt()
                        // Use the nick and score as needed
                        scoreList.add(nick to score) // Add nick and score pair to the list
                        line = bufferedReader.readLine()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Sort the list by score in descending order
        scoreList.sortByDescending { it.second }

        // Add elements to the GridLayout in order of best score
        for ((nick, score) in scoreList) {
            val textView = TextView(this)
            textView.text = "Nick: $nick, Score: $score"
            scrollView.addView(textView)
        }
    }
}
