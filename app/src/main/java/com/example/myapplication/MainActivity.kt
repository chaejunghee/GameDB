package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    //타이머
    var time = 0
    var timerTask: Timer? = null
    var sec = 0

    //초 텍스트
    lateinit var secTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        secTextView = findViewById(R.id.secTextView)

        timerTask = timer(period = 10) {
            time++

            sec = time / 100

            runOnUiThread {
                secTextView.text = "$sec"
            }

            //30하면 스크린에 31까지 나오고 화면전환이 돼서 29로 설정함
            if (sec == 29) {
                gameover()
            }
        }
    }

    //화면 전환 메소드
    fun gameover() {
        var intent = Intent(this, GameoverActivity::class.java)
        startActivity(intent)
    }
}
