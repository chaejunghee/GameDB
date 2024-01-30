package com.example.myapplication

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView

class GameoverActivity : AppCompatActivity() {
    lateinit var gameoverView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        gameoverView = findViewById(R.id.gameoverView)

        gameoverView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                var intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }
}