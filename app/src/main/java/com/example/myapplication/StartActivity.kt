package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    lateinit var startView: ImageView
    lateinit var startViewBGM: MediaPlayer
    lateinit var startSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        startView = findViewById(R.id.startView)

        startViewBGM = MediaPlayer.create(this, R.raw.startview_bgm)
        startSound = MediaPlayer.create(this, R.raw.start_sound)

        startViewBGM.start()
        startViewBGM.setLooping(true)

        startView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                startViewBGM.stop()
                startSound.start()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }
}