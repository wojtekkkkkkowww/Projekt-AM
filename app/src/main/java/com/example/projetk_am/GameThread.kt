package com.example.projetk_am

import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView)
    :Thread(){
    var runing = false
    override fun run() {
        while(runing) {

            synchronized(gameView.bullets) {
                val canvas = surfaceHolder.lockCanvas()
                val bulletsToRemove = mutableListOf<Bullet>() // Create a list to store bullets to remove
                for (bullet in gameView.bullets) {
                    if (bullet.elapsedTime >= 5000) {
                        bulletsToRemove.add(bullet) // Add the bullet to the removal list
                    }
                    gameView.update(bullet)
                }
                // Remove the bullets outside the iterator loop
                for (bullet in bulletsToRemove) {
                    gameView.removeBulletById(bullet.id)
                }
                if(runing){
                    gameView.draw(canvas)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }



}