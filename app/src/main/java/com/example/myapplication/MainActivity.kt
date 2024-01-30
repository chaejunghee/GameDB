package com.example.myapplication

import android.content.Intent
import android.content.pm.ActivityInfo
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

    lateinit var secTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        secTextView = findViewById(R.id.secTextView)

        timerTask = timer(period = 10) {
            time++

            sec = time / 100

            runOnUiThread {
                secTextView.text = "$sec"
            }
        }
    }
}
