package com.example.projetk_am

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    var weapon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bow)
    var bulletImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)
    var enemyImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ballon)
    var confettiImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.confetti)
    var health = 10
    var score = 0
    val enemyWidth = enemyImage.width.toFloat() * 0.01f
    val enemyHeight = enemyImage.height.toFloat() * 0.01f
    var gameActivity: GameActivity? = null

    private val colors =
        listOf(Color.RED, Color.BLUE, Color.GREEN, Color.parseColor("#FFA500"), Color.CYAN)
    private var surfaceCreated = false
    private var circleColor = getRandomColor()
    val bullets: MutableList<Bullet> = mutableListOf()
    val enemies: MutableList<Enemy> = mutableListOf()
    val confettiList: MutableList<Confetti> = mutableListOf()
    private val thread: GameThread
    private var amo = 20
    var difficulty = 0
    var size = 0
    var speed = 0

    var currentPeriod = 5000L
    var running = true

    fun newEnemy(): Enemy {
        val randomOffset = Random.nextFloat() * (height / 2)
        val randomPosition = (height / 2) - randomOffset + (height / 4)
        val enemy = Enemy(width - 1f, randomPosition, getRandomColor())
        val dx = Random.nextFloat() * -10f
        val dy = Random.nextFloat() * 20f - 10f
        val magnitude = sqrt(dx * dx + dy * dy)
        val normalizedDx = dx * difficulty / magnitude * 10f
        val normalizedDy = dy * difficulty / magnitude * 10f
        enemy.dx = normalizedDx
        enemy.dy = normalizedDy
        enemy.height = 15f
        enemy.width = 15f
        enemy.id = lastIndex
        lastIndex++
        return enemy
    }


    val enemyCreationThread = Thread {
        var iterations = 0
        while (running) {
            val number = Random.nextInt(4)+1
            for(i in 1 .. number){
                val enemy = newEnemy()
                enemies.add(enemy)
            }
            iterations++
            if (iterations % 2 == 0) {
                if(currentPeriod - 200L >0 )
                    currentPeriod -= 200L
            }

            try {
                val sleepDuration = abs(Random.nextLong()) % currentPeriod + currentPeriod
                Thread.sleep(sleepDuration)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    init {
        holder.addCallback(this)
        isFocusable = true
        thread = GameThread(holder, this)
        setOnTouchListener { _, event ->
            touchEvent(event)
            true
        }
    }

    private var initialAngle = 0f
    private var cannonRotation = 0f
    val cannonRadius = 100f
    var lastIndex = 0

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.WHITE)
        val centerX = width / 2f
        val centerY = height / 2f

        val cannonPaint: Paint = Paint().apply {
            color = circleColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(centerX, centerY, cannonRadius, cannonPaint)

        val rectWidth = weapon.width.toFloat() * 0.1f
        val rectHeight = weapon.height.toFloat() * 0.1f
        val rectLeft = centerX - (rectWidth / 2f)
        val rectTop = centerY - (rectHeight / 2f)
        val rectRight = centerX + (rectWidth / 2f)
        val rectBottom = centerY + (rectHeight / 2f)

        val bulletsCopy = bullets.toList()
        for (bullet in bulletsCopy) {

            val bulletWidth = bulletImage.width.toFloat() * 0.03f * (size / 2 + 1)
            val bulletHeight = bulletImage.height.toFloat() * 0.03f * (size / 2 + 1)
            val bulletLeft = bullet.x - (bulletWidth / 2f)
            val bulletTop = bullet.y - (bulletHeight / 2f)
            val bulletRight = bullet.x + (bulletWidth / 2f)
            val bulletBottom = bullet.y + (bulletHeight / 2f)

            val bulletPaint = Paint().apply {
                colorFilter = PorterDuffColorFilter(bullet.color, PorterDuff.Mode.SRC_IN)
            }
            canvas.save()
            canvas.rotate(bullet.rotation - 180f, bullet.x, bullet.y)
            canvas.drawBitmap(
                bulletImage,
                null,
                RectF(bulletLeft, bulletTop, bulletRight, bulletBottom),
                bulletPaint
            )
            canvas.restore()
        }
        val enemyCopy = enemies.toList()
        for (enemy in enemyCopy) {

            val enemyLeft = enemy.x - (enemyWidth / 2f)
            val enemyTop = enemy.y - (enemyHeight / 2f)
            val enemyRight = enemy.x + (enemyWidth / 2f)
            val enemyBottom = enemy.y + (enemyHeight / 2f)

            val enemyPaint = Paint().apply {
                colorFilter = PorterDuffColorFilter(enemy.color, PorterDuff.Mode.SRC_IN)
            }
            canvas.save()
            canvas.drawBitmap(
                enemyImage,
                null,
                RectF(enemyLeft, enemyTop, enemyRight, enemyBottom),
                enemyPaint
            )
            canvas.restore()
        }
        for (confetti in confettiList) {
            val confettiWidth = confettiImage.width.toFloat() * 0.2f
            val confettiHeight = confettiImage.height.toFloat() * 0.2f
            val confettiLeft = confetti.x - (confettiWidth / 2f)
            val confettiTop = confetti.y - (confettiHeight / 2f)
            val confettiRight = confetti.x + (confettiWidth / 2f)
            val confettiBottom = confetti.y + (confettiHeight / 2f)

            canvas.save()
            canvas.drawBitmap(
                confettiImage,
                null,
                RectF(confettiLeft, confettiTop, confettiRight, confettiBottom),
                null
            )
            canvas.restore()
        }

        canvas.save()
        canvas.rotate(cannonRotation, centerX, centerY)
        canvas.drawBitmap(weapon, null, RectF(rectLeft, rectTop, rectRight, rectBottom), null)
        canvas.restore()
    }

    fun drawCounters() {

        gameActivity!!.healthTextView.setTextColor(Color.RED)
        gameActivity!!.healthTextView.setText("Życia: $health")
        gameActivity!!.ammoTextView.text = "Ammo: $amo"
        gameActivity!!.scoreTextView.text = "Wynik: $score"
    }

    private fun touchEvent(event: MotionEvent): Boolean {
        if (surfaceCreated) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val clickY = event.y
                    val clickX = event.x
                    if (isWithinCircleBounds(clickX, clickY)) {
                        circleColor = getRandomColor()
                        amo = 20
                    } else {
                        val newAngle = calculateAngle(event.x, event.y)
                        initialAngle = newAngle

                        cannonRotation = initialAngle - 90
                        val startingX =
                            width / 2f - cannonRadius * cos(Math.toRadians((cannonRotation + 87).toDouble())).toFloat()
                        val startingY =
                            height / 2f - cannonRadius * sin(Math.toRadians((cannonRotation + 87).toDouble())).toFloat()

                        if (amo != 0) {
                            val bulletSpeed =
                                20f * (speed / 2 + 1)

                            val bulletDx =
                                -bulletSpeed * cos(Math.toRadians((cannonRotation + 90).toDouble())).toFloat()
                            val bulletDy =
                                -bulletSpeed * sin(Math.toRadians((cannonRotation + 90).toDouble())).toFloat()
                            val bullet = Bullet(
                                startingX,
                                startingY,
                                circleColor,
                                bulletImage.height.toFloat() * 0.03f * (size / 2 + 1),
                                lastIndex
                            )
                            bullet.rotation = cannonRotation
                            lastIndex++
                            bullet.dx = bulletDx
                            bullet.dy = bulletDy
                            bullets.add(bullet)
                            amo--
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val clickX = event.x
                    val clickY = event.y
                    if (!isWithinCircleBounds(clickX, clickY)) {
                        val newAngle = calculateAngle(clickX, clickY)
                        val rotationAngle = newAngle - initialAngle
                        cannonRotation += rotationAngle
                        initialAngle = newAngle
                    }
                }
            }
        }
        return true
    }

    fun update(bullet: Bullet) {

        bullet.x += bullet.dx
        bullet.y += bullet.dy

        if (bullet.x <= 0 || bullet.x >= width) {
            bullet.dx = -bullet.dx
            bullet.rotation = -bullet.rotation
        }
        if (bullet.y <= 0 || bullet.y >= height) {
            bullet.dy = -bullet.dy
            bullet.rotation = -bullet.rotation + 180f
        }
    }

    fun updateE(enemy: Enemy) {

        enemy.x += enemy.dx
        enemy.y += enemy.dy

        if (enemy.x - enemy.height <= 0) {
            if (enemy.isAlive)
                health--
            enemy.isAlive = false
        }
        if (enemy.y - enemy.height - 60f <= 0 || enemy.y + enemy.height >= height) {
            enemy.dy = -enemy.dy
        }
    }

    fun removeBulletById(id: Int) {
        synchronized(bullets) {
            val bulletToRemove = bullets.find { it.id == id }
            bulletToRemove?.let {
                bullets.remove(it)
            }
        }
    }

    fun removeEnemyById(id: Int) {
        synchronized(enemies) {
            val bulletToRemove = enemies.find { it.id == id }
            bulletToRemove?.let {
                enemies.remove(it)
            }
        }
    }

    private fun calculateAngle(x: Float, y: Float): Float {
        val centerX = width / 2f
        val centerY = height / 2f
        val angle = Math.toDegrees(
            kotlin.math.atan2(
                (centerY - y).toDouble(),
                (centerX - x).toDouble()
            )
        )
        return angle.toFloat()
    }

    private fun isWithinCircleBounds(x: Float, y: Float): Boolean {
        val centerX = width / 2f
        val centerY = height / 2f
        val cannonRadius = 100f

        val distanceSquared = (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)

        return distanceSquared <= (cannonRadius * cannonRadius)
    }

    fun getRandomColor(): Int {
        var randomIndex = Random.nextInt(colors.size)
        while (colors[randomIndex] == circleColor) {
            randomIndex = Random.nextInt(colors.size)
        }
        return colors[randomIndex]
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceCreated = true
        val canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        thread.runing = true
        thread.start()
        enemyCreationThread.start()
    }


    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.runing = false
        running = false
    }
}



