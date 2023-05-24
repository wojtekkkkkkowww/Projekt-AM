package com.example.projetk_am

import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView)
    :Thread(){
    var runing = false
    override fun run() {
      //  val startTime = System.currentTimeMillis()
        while(runing) {

            synchronized(gameView.bullets) {
                val canvas = surfaceHolder.lockCanvas()
                val iterator = gameView.bullets.iterator()
                while (iterator.hasNext()) {
                    val bullet = iterator.next()
                    if (bullet.elapsedTime >= 10000) {
                        iterator.remove() // Remove the bullet using the iterator
                    }
                    gameView.update(bullet)
                }
                gameView.draw(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }

            sleep(10)
        }
    }



}