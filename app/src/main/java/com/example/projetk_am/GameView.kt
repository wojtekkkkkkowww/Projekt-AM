package com.example.projetk_am

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    var weapon = BitmapFactory.decodeResource(resources, R.drawable.bow)
    var bulletImage = BitmapFactory.decodeResource(resources, R.drawable.arrow)
    var enemyImage = BitmapFactory.decodeResource(resources,R.drawable.ballon)
    var confettiImage = BitmapFactory.decodeResource(resources,R.drawable.confetti)
    var health = 10
    var score = 0
    val enemyWidth = enemyImage.width.toFloat() * 0.01f
    val enemyHeight = enemyImage.height.toFloat() * 0.01f

    private val colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN)
    private var surfaceCreated = false
    private var circleColor = getRandomColor()
    val bullets: MutableList<Bullet> = mutableListOf()
    val enemies: MutableList<Enemy> = mutableListOf()
    val confettiList: MutableList<Confetti> = mutableListOf()
    private val thread: GameThread
    private var amo = 20
    var difficulty = 0
    var type = 0



    val timer = Timer()

    // Define a TimerTask that adds a new enemy
    val task = object : TimerTask() {
        override fun run() {
            val enemy = Enemy(width - 1f, Random.nextFloat()*height, getRandomColor())
            val dx = Random.nextFloat() * -10f // Generate random value between -10 and 0
            val dy = Random.nextFloat() * 20f - 10f // Generate random value between -10 and 10

            val magnitude = sqrt(dx * dx + dy * dy) // Calculate the magnitude of the vector

            val normalizedDx = dx * difficulty/ magnitude * 10f // Normalize the dx value with constant speed
            val normalizedDy = dy * difficulty/ magnitude * 10f
            enemy.dx = normalizedDx
            enemy.dy = normalizedDy
            enemy.height = enemyHeight
            enemy.width =  enemyHeight
            enemy.id = lastIndex
            lastIndex++
            enemies.add(enemy)
        }
    }


// Schedule the task to run after 10 seconds (10000 milliseconds)


    init {
        holder.addCallback(this)
        isFocusable = true
        thread  = GameThread(holder,this)
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

        drawCounters(canvas)


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
        //Drawing bullets
        for (bullet in bulletsCopy) {

            val bulletWidth = bulletImage.width.toFloat() * 0.03f //* (type)  //teraz losowo są większe
            val bulletHeight = bulletImage.height.toFloat() * 0.03f //* (type)  //teraz losowo są większe
            val bulletLeft = bullet.x - (bulletWidth/2f)
            val bulletTop = bullet.y - (bulletHeight/2f)
            val bulletRight = bullet.x + (bulletWidth/2f)
            val bulletBottom = bullet.y + (bulletHeight/2f)

            val bulletPaint = Paint().apply {
                colorFilter = PorterDuffColorFilter(bullet.color, PorterDuff.Mode.SRC_IN)
            }
            canvas.save()
            canvas.rotate(bullet.rotation - 180f, bullet.x, bullet.y)

            canvas.drawBitmap(bulletImage, null, RectF(bulletLeft, bulletTop, bulletRight, bulletBottom), bulletPaint)

            canvas.restore()
        }
        val enemyCopy = enemies.toList()
        //drawing enemies
        for (enemy in enemyCopy) {

            val enemyLeft = enemy.x - (enemyWidth/2f)
            val enemyTop = enemy.y - (enemyHeight/2f)
            val enemyRight = enemy.x + (enemyWidth/2f)
            val enemyBottom = enemy.y + (enemyHeight/2f)


            val enemyPaint = Paint().apply {
                colorFilter = PorterDuffColorFilter(enemy.color, PorterDuff.Mode.SRC_IN)
            }
            canvas.save()
            canvas.drawBitmap(enemyImage, null, RectF(enemyLeft, enemyTop, enemyRight, enemyBottom), enemyPaint)

            canvas.restore()
        }
        for (confetti in confettiList) {
            val confettiWidth = confettiImage.width.toFloat() * 0.2f
            val confettiHeight = confettiImage.height.toFloat() * 0.2f
            val confettiLeft = confetti.x - (confettiWidth/2f)
            val confettiTop = confetti.y - (confettiHeight/2f)
            val confettiRight = confetti.x + (confettiWidth/2f)
            val confettiBottom = confetti.y + (confettiHeight/2f)

            canvas.save()
            canvas.drawBitmap(confettiImage, null, RectF(confettiLeft, confettiTop, confettiRight, confettiBottom), null)

            canvas.restore()
        }


        canvas.save()

        canvas.rotate(cannonRotation, centerX, centerY)

        canvas.drawBitmap(weapon, null, RectF(rectLeft, rectTop, rectRight, rectBottom), null)

        canvas.restore()

    }

    fun drawCounters(canvas: Canvas){
        val red = Paint().apply {
            color = Color.RED
            textSize = 80f
        }
        val black = Paint().apply {
            color = Color.BLACK
            textSize = 80f
        }
        canvas.drawText("Życia: $health", 50f, 220f, red)
        canvas.drawText("Ammo: $amo", 1000f, 220f, black)
        canvas.drawText("Wynik: $score", 550f, 120f, black)
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
                    }
                    else{
                        val newAngle = calculateAngle(event.x, event.y)
                        // Update the initial touch angle
                        initialAngle = newAngle

                        // Metoda prob i bledow
                        cannonRotation = initialAngle - 90
                        // Calculate starting position based on cannon rotation
                        val startingX = width / 2f - cannonRadius * cos(Math.toRadians((cannonRotation+87).toDouble())).toFloat()
                        val startingY = height / 2f - cannonRadius * sin(Math.toRadians((cannonRotation+87).toDouble())).toFloat()

                        if(amo!=0) {
                            //  val bullet = Bullet(startingX, startingY,circleColor, 10f)
                            val bulletSpeed = 20f // Adjust the bullet speed as needed

                            val bulletDx =
                                -bulletSpeed * cos(Math.toRadians((cannonRotation + 90).toDouble())).toFloat()
                            val bulletDy =
                                -bulletSpeed * sin(Math.toRadians((cannonRotation + 90).toDouble())).toFloat()
                            val bullet = Bullet(startingX, startingY, circleColor, 10f, lastIndex)
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
                    val clickX =event.x
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

        if (bullet.x <= 0 || bullet.x + bullet.radius >= width) {
            bullet.dx = -bullet.dx
            bullet.rotation = -bullet.rotation
        }
        if (bullet.y <= 0 || bullet.y + bullet.radius >= height) {
            bullet.dy = -bullet.dy
            bullet.rotation = -bullet.rotation +180f
        }
    }

    fun updateE(enemy: Enemy) {

        enemy.x += enemy.dx
        enemy.y += enemy.dy

        if (enemy.x <= 0 || enemy.x + enemy.height *0.01f>= width) {
            if(enemy.isAlive)
                health--
            enemy.isAlive = false
        }
        if (enemy.y <= 0 || enemy.y + enemy.width*0.01f >= height) {
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
        val cannonRadius = 100f // Radius of the circle

        // Calculate the squared distance between the clicked point and the center of the circle
        val distanceSquared = (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)

        // Check if the squared distance is less than or equal to the squared radius
        return distanceSquared <= (cannonRadius * cannonRadius)
    }

    fun getRandomColor(): Int {
        var randomIndex = Random.nextInt(colors.size)
        while(colors[randomIndex] == circleColor) {
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
        timer.scheduleAtFixedRate(task, 1000, Random.nextInt(10000).toLong())
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.runing = false
    }
}



