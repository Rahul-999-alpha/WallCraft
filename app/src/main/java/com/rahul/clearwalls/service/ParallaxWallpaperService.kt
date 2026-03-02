package com.rahul.clearwalls.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.io.File

class ParallaxWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = ParallaxEngine()

    inner class ParallaxEngine : Engine(), SensorEventListener {
        private var sensorManager: SensorManager? = null
        private var accelerometer: Sensor? = null
        private var wallpaperBitmap: Bitmap? = null

        private var offsetX = 0f
        private var offsetY = 0f
        private val maxOffset = 30f
        private val smoothing = 0.1f

        private val handler = Handler(Looper.getMainLooper())
        private var isVisible = false

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            loadWallpaper()
        }

        private fun loadWallpaper() {
            val prefs = getSharedPreferences("clearwalls_live", MODE_PRIVATE)
            val path = prefs.getString("live_wallpaper_path", null) ?: return
            val file = File(path)
            if (file.exists()) {
                wallpaperBitmap = BitmapFactory.decodeFile(path)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            isVisible = visible
            if (visible) {
                sensorManager?.registerListener(
                    this, accelerometer, SensorManager.SENSOR_DELAY_GAME
                )
            } else {
                sensorManager?.unregisterListener(this)
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder, format: Int, width: Int, height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val targetX = event.values[0] * (maxOffset / 10f)
                val targetY = event.values[1] * (maxOffset / 10f)

                offsetX += (targetX - offsetX) * smoothing
                offsetY += (targetY - offsetY) * smoothing

                offsetX = offsetX.coerceIn(-maxOffset, maxOffset)
                offsetY = offsetY.coerceIn(-maxOffset, maxOffset)

                drawFrame()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        private fun drawFrame() {
            val bitmap = wallpaperBitmap ?: return
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    val canvasWidth = canvas.width.toFloat()
                    val canvasHeight = canvas.height.toFloat()
                    val bitmapWidth = bitmap.width.toFloat()
                    val bitmapHeight = bitmap.height.toFloat()

                    val scale = maxOf(
                        (canvasWidth + maxOffset * 2) / bitmapWidth,
                        (canvasHeight + maxOffset * 2) / bitmapHeight
                    )

                    val matrix = Matrix().apply {
                        val scaledWidth = bitmapWidth * scale
                        val scaledHeight = bitmapHeight * scale
                        val translateX = (canvasWidth - scaledWidth) / 2f + offsetX
                        val translateY = (canvasHeight - scaledHeight) / 2f + offsetY
                        setScale(scale, scale)
                        postTranslate(translateX, translateY)
                    }

                    canvas.drawBitmap(bitmap, matrix, null)
                }
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas)
                    } catch (_: Exception) {}
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            sensorManager?.unregisterListener(this)
            wallpaperBitmap?.recycle()
            wallpaperBitmap = null
        }
    }
}
