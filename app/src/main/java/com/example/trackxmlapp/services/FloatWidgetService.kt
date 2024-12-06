package com.example.trackxmlapp.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.trackxmlapp.R
import android.provider.Settings
import android.util.Log
import androidx.cardview.widget.CardView

class FloatingWidgetService : Service() {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private lateinit var layoutParams: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null)

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(floatingView, layoutParams)

        val mainButton = floatingView?.findViewById<Button>(R.id.mainButton)
        val startButton = floatingView?.findViewById<Button>(R.id.startButton_accessibility)
        val closeButton = floatingView?.findViewById<Button>(R.id.closeButton)

        mainButton?.setOnClickListener {
            if (startButton?.visibility == View.GONE) {
                startButton.visibility = View.VISIBLE
                closeButton?.visibility = View.VISIBLE
            } else {
                startButton?.visibility = View.GONE
                closeButton?.visibility = View.GONE
            }
        }

        startButton?.setOnClickListener {
            if (checkAccessibilityPermission()) {
                if (Settings.canDrawOverlays(this)) {
                    showToast("Services started")

                    val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
                    if (launchIntent != null) {
                        startActivity(launchIntent)
                        Log.d("AccessibilityService", "Launching YouTube app.")
                    } else {
                        showToast("YouTube Not Found")
                        Log.d("AccessibilityService", "YouTube app not found.")
                    }

                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        closeButton?.setOnClickListener {
            stopSelf()
        }

        mainButton?.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Simpan posisi awal tombol
                        initialX = layoutParams.x
                        initialY = layoutParams.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                        layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager?.updateViewLayout(floatingView, layoutParams)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        v?.performClick()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun checkAccessibilityPermission(): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        return accessEnabled != 0
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let {
            windowManager?.removeView(it)
            floatingView = null
        }
    }
}