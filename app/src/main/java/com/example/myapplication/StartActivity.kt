package com.example.myapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class StartActivity : AppCompatActivity() {
    lateinit var startView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        startView = findViewById(R.id.startView)

        startView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }
}