package com.example.projetk_am

class Bullet(
 var x: Float,
 var y: Float,
 var color: Int,
 var radius: Float,
 val id:Int,
 private val creationTime: Long = System.currentTimeMillis()
 ) {
 var rotation =0f
 var dx = 10f
 var dy = 10f
 val elapsedTime: Long
  get() = System.currentTimeMillis() - creationTime

}