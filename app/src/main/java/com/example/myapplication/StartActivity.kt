package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    lateinit var startView: ImageView       //시작화면 이미지
    lateinit var startViewBGM: MediaPlayer  //시작화면 배경음
    lateinit var startSound: MediaPlayer    //시작 효과음

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        startView = findViewById(R.id.startView)

        //시작화면 배경음과 시작 효과음 설정
        startViewBGM = MediaPlayer.create(this, R.raw.startview_bgm)
        startSound = MediaPlayer.create(this, R.raw.start_sound)

        //시작화면 배경음 무한루프로 재생
        startViewBGM.start()
        startViewBGM.setLooping(true)

        //화면 터치 시 시작 배경음 종료, 시작 효과음 재생, 게임 화면으로 전환
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