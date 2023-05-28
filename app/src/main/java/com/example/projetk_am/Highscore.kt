package com.example.projetk_am

import android.util.Log
import com.orm.SugarRecord

class Highscore : SugarRecord(){

    private val highScoreList: MutableMap<String, Int> = HashMap()
    fun addScore(nick: String, score: Int) {
        highScoreList[nick] = score
    }
    fun getHighScoreList(): Map<String, Int> {
        return highScoreList
    }
    fun getSortedHighScores(): List<Pair<String, Int>> {
        return highScoreList.toList().sortedBy { (_, score) -> score }.reversed()
    }
    fun printTop10(){
        val top10 = getSortedHighScores().take(10)
        top10.forEachIndexed { index, (nick, score) ->
            Log.i("test", "${index + 1}. $nick --- $score")
        }
    }
}