package com.example.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class StartActivity : AppCompatActivity() {
    lateinit var startView: ImageView       //시작화면 이미지
    lateinit var startViewBGM: MediaPlayer  //시작화면 배경음
    lateinit var startSound: MediaPlayer    //시작 효과음
    lateinit var startText: TextView         //시작화면 텍스트 ("화면을 터치하면 시작합니다")

    override fun onCreate(savedInstanceState: Bundle?) {
        //status bar 검정색으로 변경
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        startView = findViewById(R.id.startView)
        startText = findViewById(R.id.startText)

        //시작화면 배경음과 시작 효과음 설정
        startViewBGM = MediaPlayer.create(this, R.raw.startview_bgm)
        startSound = MediaPlayer.create(this, R.raw.start_sound)

        //시작화면 배경음 무한루프로 재생
        startViewBGM.start()
        startViewBGM.setLooping(true)

        //텍스트가 깜빡거리는 애니메이션 설정
        val anim = ObjectAnimator.ofFloat(startText, "alpha", 0f, 1f)
        //애니메이션 한 사이클의 시간 (0.5초)
        anim.duration = 500
        //애니메이션 반복 방식 (REVERSE: 순방향, 역방향 번갈아가며)
        anim.repeatMode = ObjectAnimator.REVERSE
        // 애니메이션 무한 반복
        anim.repeatCount = ObjectAnimator.INFINITE
        // 애니메이션 시작
        anim.start()

        //화면 터치 시 시작 배경음 종료, 시작 효과음 재생, 게임 화면으로 전환
        startView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                startViewBGM.stop()
                startSound.start()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                //액티비티 종료
                finish()

                //애니메이션 해제
                anim.cancel()
            }
            true
        }
    }

    //액티비티 종료 시 시작 배경음 반납, 시작 효과음 반납
    override fun onDestroy() {
        startViewBGM.release()
        startSound.release()
        super.onDestroy()
    }
}