package com.example.projetk_am

class Confetti(
    var x: Float,
    var y: Float,
    private val creationTime: Long = System.currentTimeMillis()
) {
    var rotation =0f
    var dx = 10f
    var dy = 10f
    val elapsedTime: Long
        get() = System.currentTimeMillis() - creationTime

}