package com.example.myapplication

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var btn_left: Button
    lateinit var btn_right: Button
    lateinit var btn_up: Button
    lateinit var btn_down: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_left = findViewById(R.id.btn_left)
        btn_right = findViewById(R.id.btn_right)
        btn_up = findViewById(R.id.btn_up)
        btn_down = findViewById(R.id.btn_down)
    }
}