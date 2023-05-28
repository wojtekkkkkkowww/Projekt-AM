package com.example.projetk_am

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Highscore : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.highscore_activity)
        val backButton : Button = findViewById(R.id.backButton)
        backButton.setOnClickListener{
             finish()
             overridePendingTransition(0,0)
        }
    }

    /*fun save(nick: String, wynik:Int){

        with(sharedPref.edit()){
            putString(nick,wynik.toString())
            apply()
        }
    }

    fun remove(nick: String){
        val sharedPref = context.getSharedPreferences("highscores", 0)
        with(sharedPref.edit()){
            remove(nick)
            apply()
        }
    }

    fun get(nick: String): Int {
        val sharedPref = contex.getSharedPreferences("highscores", 0)
        return Integer.parseInt(sharedPref.getString(nick, 0).toString())
    }**/

}