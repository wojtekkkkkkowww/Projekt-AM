package com.example.projetk_am

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN)
    private var surfaceCreated = false
    private var circleColor = getRandomColor()
    val bullets: MutableList<Bullet> = mutableListOf()
    private val thread: GameThread

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

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)
        val rectWidth = bitmap.width.toFloat() * 0.1f
        val rectHeight = bitmap.height.toFloat() * 0.1f
        val rectLeft = centerX - (rectWidth / 2f)
        val rectTop = centerY - (rectHeight / 2f)
        val rectRight = centerX + (rectWidth / 2f)
        val rectBottom = centerY + (rectHeight / 2f)

        for (bullet in bullets) {
            val bulletPaint = Paint().apply {
                color = bullet.color
                style = Paint.Style.FILL
            }

            canvas.drawCircle(bullet.x, bullet.y,bullet.radius,bulletPaint)
        }

        canvas.save()

        canvas.rotate(cannonRotation, centerX, centerY)

        canvas.drawBitmap(bitmap, null, RectF(rectLeft, rectTop, rectRight, rectBottom), null)

        canvas.restore()
    }




    fun touchEvent(event: MotionEvent): Boolean {
        if (surfaceCreated) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val clickY = event.y
                    val clickX = event.x
                    if (isWithinCircleBounds(clickX, clickY)) {
                        circleColor = getRandomColor()

                    }
                    else{
                        val newAngle = calculateAngle(event.x, event.y)
                        // Update the initial touch angle
                        initialAngle = newAngle

                        // Metoda prob i bledow
                        cannonRotation = initialAngle - 90
                        // Calculate starting position based on cannon rotation
                        val startingX = width / 2f - cannonRadius * cos(Math.toRadians((cannonRotation+90).toDouble())).toFloat()
                        val startingY = height / 2f - cannonRadius * sin(Math.toRadians((cannonRotation+90).toDouble())).toFloat()


                      //  val bullet = Bullet(startingX, startingY,circleColor, 10f)
                        val bulletSpeed = 10f // Adjust the bullet speed as needed
                        val bulletDx = -bulletSpeed * cos(Math.toRadians((cannonRotation+90).toDouble())).toFloat()
                        val bulletDy = -bulletSpeed * sin(Math.toRadians((cannonRotation+90).toDouble())).toFloat()

                        val bullet = Bullet(startingX, startingY, circleColor, 10f)
                        bullet.dx = bulletDx
                        bullet.dy = bulletDy
                        bullets.add(bullet)

                    }
                    }
                MotionEvent.ACTION_MOVE -> {
                    val newAngle = calculateAngle(event.x, event.y)
                    val rotationAngle = newAngle - initialAngle
                    cannonRotation += rotationAngle
                    initialAngle = newAngle

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
        }
        if (bullet.y <= 0 || bullet.y + bullet.radius >= height) {
            bullet.dy = -bullet.dy
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
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.runing = false

    }
}



