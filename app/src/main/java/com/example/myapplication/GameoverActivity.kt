package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class GameoverActivity : AppCompatActivity() {
    lateinit var gameoverView: ImageView
    lateinit var gameoverBGM: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        gameoverView = findViewById(R.id.gameoverView)

        gameoverBGM = MediaPlayer.create(this, R.raw.gameover_bgm)
        //gameoverBGM.start()

        gameoverView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                var intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }
}