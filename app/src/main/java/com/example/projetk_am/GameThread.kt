package com.example.projetk_am

import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) :
    Thread() {
    var runing = false
    override fun run() {
        while (runing) {

            synchronized(surfaceHolder) {
                val canvas = surfaceHolder.lockCanvas()
                val bulletsToRemove =
                    mutableListOf<Bullet>() // Create a list to store bullets to remove
                val enemiesToRemove =
                    mutableListOf<Enemy>() // Create a list to store bullets to remove

                for (bullet in gameView.bullets) {
                    if (bullet.elapsedTime >= 5000) {
                        bulletsToRemove.add(bullet) // Add the bullet to the removal list
                    }
                    gameView.update(bullet)
                }
                // Remove the bullets outside the iterator loop


                for (enemy in gameView.enemies) {

                    for (bullet in gameView.bullets) {
                        // Calculate the coordinates and sizes for the enemy and bullet
                        val enemyLeft = enemy.x
                        val enemyRight = enemy.x + enemy.width
                        val enemyTop = enemy.y
                        val enemyBottom = enemy.y + enemy.height

                        val bulletLeft = bullet.x - bullet.radius
                        val bulletRight = bullet.x + bullet.radius
                        val bulletTop = bullet.y - bullet.radius
                        val bulletBottom = bullet.y + bullet.radius

// Check for collision
                        if (enemyRight > bulletLeft && enemyLeft < bulletRight &&
                            enemyBottom > bulletTop && enemyTop < bulletBottom && bullet.color == enemy.color
                        ) {
                            enemy.isAlive = false
                            bulletsToRemove.add(bullet)
                            val confetti = Confetti(enemy.x, enemy.y)
                            gameView.confettiList.add(confetti)
                            // Handle the collision logic here
                        }
                    }
                    if (!enemy.isAlive) {
                        enemiesToRemove.add(enemy)
                    }
                    gameView.updateE(enemy)
                }


                    for (bullet in bulletsToRemove) {
                        gameView.removeBulletById(bullet.id)
                    }
                    for (enemy in enemiesToRemove) {
                        gameView.removeEnemyById(enemy.id)
                    }
                    val iterator = gameView.confettiList.iterator()
                    while (iterator.hasNext()) {
                        val confetti = iterator.next()
                        if (confetti.elapsedTime >= 200) {
                            iterator.remove() // Remove the confetti from the list
                        }
                    }


                if (runing) {
                    gameView.draw(canvas)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
                sleep(10)

            }
        }
    }


}