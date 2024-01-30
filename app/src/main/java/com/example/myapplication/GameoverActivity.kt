package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class GameoverActivity : AppCompatActivity() {
    lateinit var gameoverView: ImageView    //게임오버화면 이미지
    lateinit var gameoverBGM: MediaPlayer   //게임오버 효과음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        gameoverView = findViewById(R.id.gameoverView)

        //게임오버 효과음 설정
        gameoverBGM = MediaPlayer.create(this, R.raw.gameover_bgm)
        //게임 효과음 재생
        //gameoverBGM.start()

        //화면 터치 시 시작화면으로 돌아감
        gameoverView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                var intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
            true
        }
    }
}