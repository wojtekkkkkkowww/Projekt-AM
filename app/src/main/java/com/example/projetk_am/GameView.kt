package com.example.projetk_am

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var surfaceCreated = false
    private val cannonPaint: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }

    init {
        holder.addCallback(this)
        isFocusable = true
        setOnTouchListener { _, event ->
            onTouchEvent(event, holder)
            true
        }
    }

    private var initialAngle = 0f
    private var cannonRotation = 0f

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.WHITE)
        val centerX = width / 2f
        val centerY = height / 2f

        val cannonRadius = 100f
        canvas.drawCircle(centerX, centerY, cannonRadius, cannonPaint)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cannon)

        val rectWidth = bitmap.width.toFloat() * 0.1f
        val rectHeight = bitmap.height.toFloat() * 0.1f
        val rectLeft = centerX - (rectWidth / 2f)
        val rectTop = centerY - (rectHeight / 2f)
        val rectRight = centerX + (rectWidth / 2f)
        val rectBottom = centerY + (rectHeight / 2f)
        canvas.save()

        canvas.rotate(cannonRotation, centerX, centerY)

        canvas.drawBitmap(bitmap, null, RectF(rectLeft, rectTop, rectRight, rectBottom), null)

        canvas.restore()
    }


    fun onTouchEvent(event: MotionEvent, holder: SurfaceHolder): Boolean {
        if (surfaceCreated) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val newAngle = calculateAngle(event.x, event.y)
                    // Update the initial touch angle
                    initialAngle = newAngle

                    // Metoda prob i bledow
                    cannonRotation = initialAngle - 90
                    val canvas = holder.lockCanvas()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newAngle = calculateAngle(event.x, event.y)
                    val rotationAngle = newAngle - initialAngle
                    cannonRotation += rotationAngle
                    initialAngle = newAngle
                    val canvas = holder.lockCanvas()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
        return true
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


    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceCreated = true


        var canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        var canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}



