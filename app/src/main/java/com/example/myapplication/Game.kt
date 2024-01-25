package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

open class Game : View {
    var scrw = 0  //스크린 너비 변수
    var scrh = 0  //스크린 높이 변수
    var x = 0     //캐릭터 x좌표
    var y = 0     //캐릭터 y좌표

    val paint = Paint()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    //뷰 크기가 변경되면 호출되는 메소드
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.scrw = w   //현재 스크린 너비값 설정
        this.scrh = h   //현재 스크린 높이값 설정
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.BLACK
        paint.textSize = scrh / 16f
        canvas?.drawText("너비:$scrw 높이:$scrh", 0f, scrh / 16f, paint)
    }
}