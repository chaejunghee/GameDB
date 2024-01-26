package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

//공용 클래스 Game의 뷰 확장
open class Game1(con: Context?, at: AttributeSet?) : View(con, at) {
    /*    *//* 테이블 내용을 정의한다. *//*
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "entry"
        const val _ID = "_id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
        const val COLUMN_NAME_SUBTITLE2 = "subtitle2"
    }

    inner class FeedReaderDbHelper(context: Context?) :
        SQLiteOpenHelper(context, Companion.DATABASE_NAME, null, Companion.DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(Game1.Companion.SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(Game1.Companion.SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1 //DB 버전
            const val DATABASE_NAME = "FeedReader.db" //DB 이름
        }
    }

    var dbHelper = FeedReaderDbHelper(getContext())*/

    var scrw = 0    //스크린 너비값을 저장할 변수
    var scrh = 0    //스크린 높이값을 저장할 변수

    var xd = -120f //캐릭터 이미지 기준점 x좌표 저장할 변수 (*게임 시작 시 중앙에 배치하기 위해 임의로 -120f값 넣음)
    var yd = -80f   //캐릭터 이미지 기준점 y좌표 저장할 변수 (*게임 시작 시 중앙에 배치하기 위해 임의로 -80f값 넣음)

    //적군의 x좌표 값을 저장할 실수형 변수
    var rxd = FloatArray(3)

    //적군의 y좌표 값을 저장할 실수형 변수
    var ryd = FloatArray(3)

    //적군의 이동을 위한 카운트 수를 저장할 정수형 변수
    var count2 = IntArray(3)

    //적군의 생명 값을 저장할 정수형 변수
    var life = IntArray(3)

    //적군의 이동 방향을 저장할 문자열 변수
    private val RectDirButton = arrayOfNulls<String>(3)

    // 위에서 3은 적군을 화면에 최대 3개까지만 표시할 것을 의미합니다.
    //캐릭터의 이동을 위한 카운트 수를 저장할 정수형 변수
    var count = 0

    //DB저장
    var save = 0
    var savecount = 1

    //캐릭터 방향키 버튼 클릭 유무 (false(=0)(*안 누름) 또는 true(=1)(*누름))
    var start = false

    //캐릭터가 정지 상태에 있는 동안 어떤 방향키를 클릭했는지 저장할 문자열 변수
    private var DirButton: String? = null

    //캐릭터가 이동하고 있는 상태에서 어떤 방향키를 클릭했는지 저장할 문자열 변수
    private var DirButton2: String? = null

    //화면에 표시할 수 있는 최대 미사일 수를 저장할 정수형 변수
    var missileCount = 0

    //미사일 번호를 저장할 정수형 변수
    var missileNum = IntArray(10)

    //미사일 x위치를 저장할 정수형 변수
    var mx = FloatArray(10)

    //미사일 y위치를 저장할 정수형 변수
    var my = FloatArray(10)

    //미사일 방향을 저장할 정수형 변수
    var md = IntArray(10)

    //미사일 초기 방향을 저장할 정수형 변수. 4번 즉 아래 방향으로 설정함.
    var MD = 4

    //랜덤 변수
    var random = Random

    //위에서 10은 화면에 최대 10개까지의 미사일을 표시할 것을 의미합니다.

    //화면의 폭을 왼쪽과 오른쪽 부분 중 어느 곳을 클릭했는지 판별하는데 사용할 정수형 변수
    var n = 0

    //p라는 이름의 페인트 변수 설정
    var paint: Paint = Paint()

    //T라는 이름의 게임 쓰레드를 설정.
    private var thread: GameThread? = null


    //배경음 재생을 위해 MediaPlayer 사용
    var bgBGM: MediaPlayer

    lateinit var player: Bitmap
    lateinit var moveButton: Bitmap
    lateinit var attackButton: Bitmap
    lateinit var enemy: Bitmap

    //Game이란 이름의 생성자 생성 -> Context는 앱에 대한 다양한 정보가 들어 있다. AttributeSet은 xml정보를 가져온다.
    init {
        //부모 클래스의 생성자를 불러와서 초기화시킨다.

        //사용할 파일 경로 연결
        bgBGM = MediaPlayer.create(con, R.raw.background_bgm)
        //background_bgm파일 재생
        bgBGM.start()
        //background_bgm파일 무한재생
        bgBGM.setLooping(true)
    }

    //뷰의 크기가 변경될 때 호출
    protected override fun onSizeChanged(sw: Int, sh: Int, esw: Int, esh: Int) {
        //부모 클래스의 멤버 변수를 참조한다.
        super.onSizeChanged(sw, sh, esw, esh)
        //뷰의 폭 정보 저장
        scrw = sw
        //뷰의 높이 정보 저장
        scrh = sh

        /*//적군의 생명력 저장
        for (i in 0..2) {
            //적군이 미사일에 2번까지 견디도록 설정
            life[i] = 2
        }*/

        //쓰레드 값이 비었다면
        if (thread == null) {
            //GameThread()함수를 돌린 값을 넣어줌
            thread = GameThread()
            //쓰레드 시작
            thread!!.start()
        }
    }

    // 뷰가 윈도우에서 분리될 때마다 발생.
    protected override fun onDetachedFromWindow() {
        //쓰레드의 run 값으로 false 값을 줌.
        thread!!.run = false
        //부모 클래스의 멤버 변수를 참조
        super.onDetachedFromWindow()
    }

    //캔버스 위에 그리기
    protected override fun onDraw(canvas: Canvas) {

        /*//테이블 내에 있는 DB 삭제
        if (count == 0 && DirButton === "Up") {
            val db: SQLiteDatabase = dbHelper.getWritableDatabase()
            db.execSQL("DELETE FROM entry")
            savecount = 1
        }

        //테이블 title과 subtitle에 저장할 내용
        val title = "" + savecount
        val subtitle = "5678$savecount"
        val subtitle2 = "서브2 :$savecount"

        //DB 저장
        if (count == 0 && DirButton === "Left" && save == 0) {
            // Gets the data repository in write mode
            val db: SQLiteDatabase = dbHelper.getWritableDatabase()

            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(FeedEntry.COLUMN_NAME_TITLE, title)
            values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle)
            values.put(FeedEntry.COLUMN_NAME_SUBTITLE2, subtitle2)

            // Insert the new row, returning the primary key value of the new row
            val newRowId: Long = db.insert(FeedEntry.TABLE_NAME, null, values)
            save = 1
            savecount += 1
            db.close()
        }
        if (count == 0 && DirButton === "Down") {
            save = 0
        }

        //DB 읽기
        if (count == 0 && DirButton === "Right") {
            val db: SQLiteDatabase = dbHelper.getReadableDatabase()
            val a = "1"
            val cursor1: Cursor = db.rawQuery("SELECT * FROM entry WHERE _id =$a", null)
            if (cursor1.moveToFirst()) {
                val Title: String = cursor1.getString(2)
                p.setTextSize(scrh / 16f)
                canvas.drawText("" + Title, 0f, 400f, p)
            }
            cursor1.close()
            db.close()
        }*/

        when (n) {
            //게임시작 시
            0 -> {
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player04)
                player = Bitmap.createScaledBitmap(player, scrw / 8, scrh / 4, true)
                canvas.drawBitmap(player, (scrw / 2f) + xd, (scrh / 2f) + yd, null)
            }
            //좌
            in 1..3 -> {
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player01)
                player = Bitmap.createScaledBitmap(player, scrw / 8, scrh / 4, true)
                canvas.drawBitmap(player, (scrw / 2f) + xd, (scrh / 2f) + yd, null)
            }
            //우
            in 4..6 -> {
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player02)
                player = Bitmap.createScaledBitmap(player, scrw / 8, scrh / 4, true)
                canvas.drawBitmap(player, (scrw / 2f) + xd, (scrh / 2f) + yd, null)
            }
            //상
            in 7..9 -> {
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player03)
                player = Bitmap.createScaledBitmap(player, scrw / 8, scrh / 4, true)
                canvas.drawBitmap(player, (scrw / 2f) + xd, (scrh / 2f) + yd, null)
            }
            //하
            in 10..12 -> {
                player = BitmapFactory.decodeResource(getResources(), R.drawable.player04)
                player = Bitmap.createScaledBitmap(player, scrw / 8, scrh / 4, true)
                canvas.drawBitmap(player, (scrw / 2f) + xd, (scrh / 2f) + yd, null)
            }
        }

        enemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy01)
        enemy = Bitmap.createScaledBitmap(enemy, scrw / 8, scrh / 4, true)
        canvas.drawBitmap(enemy, 0f, 0f, null)

        /*//i는 0부터 3보다 작은 동안 i는 1씩 증가
        for (i in 0..2) {
            //rect3라는 이름의 페인트 변수 생성
            val rect3 = Paint()
            //rect3 페인트 변수 색상을 빨간색으로 설정
            rect3.setColor(Color.RED)*/


        /*//적군의 생명이 있다면, 적군을 그려준다. (왼쪽, 위, 오른쪽, 아래) 좌표를 사용하여 상자를 그려준다.
        if (life[i] > 0) canvas.drawRect(
            scrw / 2 + rxd[i],
            scrh / 2 + ryd[i],
            scrw / 2 + (scrw - scrw % 64) / 8 + rxd[i],
            scrh / 2 + (scrh - scrh % 32) / 4 + ryd[i],
            rect3
        )
    }*/

        //missile에 10개가지의 비트맵 정보를 저장함
        val missile: Array<Bitmap?> = arrayOfNulls<Bitmap>(10)

        //미사일 수는 0으로 설정
        missileCount = 0

        //i가 0부터 10보다 작은 동안 i가 1씩 증가하면서 반복문 처리
        for (i in 0..9) {
            //i번째 미사일 번호가 0이라는 값을 품고 있으면(미사일 비활성)
            if (missileNum[i] == 0) {
                //미사일 카운트를 1 증가
                missileCount += 1
            }

            //i번째 미사일 번호가 1이라는 값을 품고 있으면(미사일 활성)
            if (missileNum[i] == 1) {
                //좌
                //i번째 md가 1이라면
                if (md[i] == 1) {
                    missile[i] = BitmapFactory.decodeResource(getResources(), R.drawable.missile01)
                    //i번째 미사일 그림 파일의 화면에 나타낼 크기 설정
                    missile[i] = Bitmap.createScaledBitmap(missile[i]!!, scrw / 16, scrw / 16, true)
                    canvas.drawBitmap(missile[i]!!, mx[i], my[i], null)

                    //i번째 mx값을 scrw/64만큼 감소
                    mx[i] -= (scrw / 64).toFloat()
                }
                //우
                //i번째 md가 2라면
                if (md[i] == 2) {
                    missile[i] = BitmapFactory.decodeResource(getResources(), R.drawable.missile02)
                    //i번째 미사일 그림 파일의 화면에 나타낼 크기 설정
                    missile[i] = Bitmap.createScaledBitmap(missile[i]!!, scrw / 16, scrw / 16, true)
                    canvas.drawBitmap(missile[i]!!, mx[i], my[i], null)

                    //i번째 mx값을 scrw/64만큼 증가
                    mx[i] += (scrw / 64).toFloat()
                }
                //상
                //i번재 md가 3이라면
                if (md[i] == 3) {
                    missile[i] = BitmapFactory.decodeResource(getResources(), R.drawable.missile03)
                    //i번째 미사일 그림 파일의 화면에 나타낼 크기 설정
                    missile[i] = Bitmap.createScaledBitmap(missile[i]!!, scrw / 16, scrw / 16, true)
                    canvas.drawBitmap(missile[i]!!, mx[i], my[i], null)

                    //i번째 my값을 scrh/32만큼 감소
                    my[i] -= (scrh / 32).toFloat()
                }
                //하
                //i번째 md가 4라면
                if (md[i] == 4) {
                    missile[i] = BitmapFactory.decodeResource(getResources(), R.drawable.missile04)
                    //i번째 미사일 그림 파일의 화면에 나타낼 크기 설정
                    missile[i] = Bitmap.createScaledBitmap(missile[i]!!, scrw / 16, scrw / 16, true)
                    canvas.drawBitmap(missile[i]!!, mx[i], my[i], null)

                    //i번재 my값을 scrh/32만큼 증가
                    my[i] += (scrh / 32).toFloat()
                }

                /*//j는 0부터 j는 3보다 작은 동안 1씩 증가
                for (j in 0..2) {
                    //생명력이 존재하는 j번째 적군 캐릭터가 i번째 미사일에 맞았다면
                    if (
                        life[j] > 0
                        && mx[i] <= scrw / 2 + (scrw - scrw % 64) / 8 + rxd[j]
                        && mx[i] >= scrw / 2 + rxd[j]           //scrw/2+rxd[j] <= mx[i] <= scrw/2+(scrw-scrw%64) / 8 + rxd[j]
                        && my[i] >= scrh / 2 + ryd[j]           //scrh/2+ryd[j] <= my[i] <= scrh/2+(scrh-scrh%32) / 4 + ryd[j]
                        && my[i] <= scrh / 2 + (scrh - scrh % 32) / 4 + ryd[j]
                    ) {
                        //j번째 적군의 생명력 1감소
                        life[j] -= 1
                        //i번째 미사일을 비활성화 시킨다.
                        missileNum[i] = 0
                    }
                }*/
            }

            //i번째 mx값이 scrw-scrw/16보다 크거나 i번째 mx값이 0보다 작거나 i번째 my값이 scrh-scrw/16보다 크거나 i번째 my값이 0보다 작다면
            if (mx[i] > scrw - scrw / 16 || mx[i] < 0 || my[i] > scrh - scrw / 16 || my[i] < 0) {
                //i번째 미사일을 비활성화 시킨다.
                missileNum[i] = 0
            }
        }

        //방향키 버튼
        moveButton = BitmapFactory.decodeResource(getResources(), R.drawable.move_button)
        moveButton = Bitmap.createScaledBitmap(moveButton, scrw / 16 * 3, scrh / 8 * 3, true)
        canvas.drawBitmap(moveButton, 0f, (scrh - moveButton.height).toFloat(), null)

        //공격 버튼
        attackButton = BitmapFactory.decodeResource(getResources(), R.drawable.attack_button)
        attackButton = Bitmap.createScaledBitmap(attackButton, scrw / 16 * 3, scrh / 8 * 3, true)
        canvas.drawBitmap(
            attackButton,
            (scrw - attackButton.width).toFloat(),
            (scrh - attackButton.height).toFloat(),
            null
        )
    }

    //터치이벤트 처리
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //만약 회면을 터치했다면
        if (event.getAction() == MotionEvent.ACTION_MOVE
            || event.getAction() == MotionEvent.ACTION_DOWN
            || event.getAction() == MotionEvent.ACTION_POINTER_DOWN
        ) {
            //좌
            if (event.getX().toInt() > 15 && event.getX().toInt() < 115
                && event.getY().toInt() > 715 && event.getY().toInt() < 835
            ) {
                //버튼을 클릭한 상태가 아니고, 캐릭터가 이동중이 아니라면
                if (start == false && count == 0) {
                    //버튼을 클릭했음을 알림
                    start = true
                    //왼쪽 버튼을 클릭헸음을 알림
                    DirButton = "Left"
                }
                //왼쪽 버튼을 클릭헸음을 알림
                DirButton2 = "Left"
            }
            //우
            else if (event.getX().toInt() > 235 && event.getX().toInt() < 335
                && event.getY().toInt() > 715 && event.getY().toInt() < 835
            ) {
                //버튼을 클릭한 상태가 아니고, 캐릭터가 이동중이 아니라면
                if (start == false && count == 0) {
                    //버튼을 클릭했음을 알림
                    start = true
                    //오른쪽 버튼을 클릭헸음을 알림
                    DirButton = "Right"
                }
                //오른쪽 버튼을 클릭했음을 알림
                DirButton2 = "Right"
            }
            //상
            else if (event.getX().toInt() > 115 && event.getX().toInt() < 235
                && event.getY().toInt() > 615 && event.getY().toInt() < 715
            ) {
                //버튼을 클릭한 상태가 아니고, 캐릭터가 이동중이 아니라면
                if (start == false && count == 0) {
                    //버튼을 클릭했음을 알림
                    start = true
                    //위쪽 버튼을 클릭헸음을 알림
                    DirButton = "Up"
                }
                //위쪽 버튼을 클릭헸음을 알림
                DirButton2 = "Up"
            }
            //하
            else if (event.getX().toInt() > 115 && event.getX().toInt() < 235
                && event.getY().toInt() > 835 && event.getY().toInt() < 935
            ) {
                //버튼을 클릭한 상태가 아니고, 캐릭터가 이동중이 아니라면
                if (start == false && count == 0) {
                    //버튼을 클릭했음을 알림
                    start = true
                    //아래 버튼을 클릭헸음을 알림
                    DirButton = "Down"
                }
                //아래 버튼을 클릭헸음을 알림
                DirButton2 = "Down"
            }
            //공격
            else if (event.getX().toInt() > scrw - attackButton.width
                && event.getX().toInt() < scrw
                && event.getY().toInt() > scrh - attackButton.height
                && event.getY().toInt() < scrh
            ) {
                //i는 0부터 i는 10보다 작은 동안 i는 1씩 증가
                for (i in 0..9) {
                    //i번째 미사일이 비활성화 상태라면
                    if (missileNum[i] == 0) {
                        //i번째 미사일에 x좌표값을 부여한다.
                        mx[i] = scrw / 2 + (scrw / 8 - scrw / 16) / 2 + xd
                        //i번째 미사일에 y좌표값을 부여한다.
                        my[i] = scrh / 2 + (scrh / 4 - scrw / 16) / 2 + yd
                        //i번째 미사일 방향을 저장한다.
                        md[i] = MD
                        //i번째 미사일을 활성화시킨다.
                        missileNum[i] = 1
                        //잔여 미사일 숫자가 0이 아니라면, 미사일 숫자를 1감소시킨다.
                        if (missileCount != 0) missileCount -= 1
                        //함수 종료
                        break
                    }
                }
            } else {
                //버튼을 클릭하고 있는 상태가 아니라고 선언함
                start = false
            }
        }

        //화면에서 손을 땠을때
        if (event.getAction() == MotionEvent.ACTION_UP
            || event.getAction() == MotionEvent.ACTION_POINTER_UP
        ) {
            //좌
            if (event.getX().toInt() > 15 && event.getX().toInt() < 115
                && event.getY().toInt() > 715 && event.getY().toInt() < 835
            ) {
                //방향키 버튼을 클릭하고 있는 상태가 아님을  선언
                start = false
            }
            //우
            else if (event.getX().toInt() > 235 && event.getX().toInt() < 335
                && event.getY().toInt() > 715 && event.getY().toInt() < 835
            ) {
                //방향키 버튼을 클릭하고 있는 상태가 아님을  선언
                start = false
            }
            //상
            else if (event.getX().toInt() > 115 && event.getX().toInt() < 235
                && event.getY().toInt() > 615 && event.getY().toInt() < 715
            ) {
                //방향키 버튼을 클릭하고 있는 상태가 아님을  선언
                start = false
            }
            //하
            else if (event.getX().toInt() > 115 && event.getX().toInt() < 235
                && event.getY().toInt() > 835 && event.getY().toInt() < 935
            ) {
                //방향키 버튼을 클릭하고 있는 상태가 아님을  선언
                start = false
            }
        }

        return true
    }


    /*    companion object {
    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +  //테이블 이름
                FeedEntry._ID + " INTEGER PRIMARY KEY," +  //고유 번호 키 부여한 ID값
                FeedEntry.COLUMN_NAME_TITLE + " TEXT," +  //텍스트 형식의 타이틀 이름
                FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT," +
                FeedEntry.COLUMN_NAME_SUBTITLE2 + " TEXT)" //텍스트 형식의 보조 타이블 이름
    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME
}*/

    internal inner class GameThread : Thread() {
        //run은 0 또는 1의 값을 가질 수 있으며, true 값을 넣어줌. (true = 1, false = 0)
        var run = true

        //run 함수를 만들어줌
        override fun run() {
            //run의 값이 1인 동안
            while (run) {
                try {
                    //뷰에서 이미지를 분리시킨다.
                    postInvalidate()
                    //카운트가 8이라면
                    if (count == 8) {
                        //카운트는 0
                        count = 0
                        //방향키 정보를 저장한다.
                        DirButton = DirButton2
                    }

                    //방향키를 클릭한 상태이고, 카운트 수가 0이라면
                    if (start == true && count == 0) {
                        //카운트 수는 1씩 증가한다.
                        count += 1
                        //또한
                    } else {
                        //카운트 수가 0보다 크고, 8보다 작다면 카운트 수는 1씩 증가한다.
                        if (count > 0 && count < 8) count += 1
                    }

                    //i는 0부터 3보다 작은 동안 i는 1씩 증가
                    for (i in 0..2) {
                        //i번째 count2가 8이라면
                        if (count2[i] == 8) {
                            //i번째 count2는 0
                            count2[i] = 0
                        }
                        //i번째 count2가 0이라면
                        if (count2[i] == 0) {
                            //정수형 변수r은 1~4 사이의 정수 값을 가진다.
                            val r: Int = random.nextInt(4) + 1      //1,2,3,4

                            //좌
                            //r이 1이라면
                            if (r == 1) {
                                //i번째 RectDirButton은 왼쪽임을 저장
                                RectDirButton[i] = "Left"
                            }
                            //우
                            //r이 2라면
                            if (r == 2) {
                                //i번째 RectDirButton은 오른쪽임을 저장
                                RectDirButton[i] = "Right"
                            }
                            //상
                            //r이 3이라면
                            if (r == 3) {
                                //i번째 RectDirButton은 위쪽임을 저장
                                RectDirButton[i] = "Up"
                            }
                            //하
                            //r이 4라면
                            if (r == 4) {
                                //i번째 RectDirButton은 아래쪽임을 저장
                                RectDirButton[i] = "Down"
                            }
                        }
                        /*                        //i번째 생명이 남아있고, i번째 방향이 아래쪽이라면
                                                if (life[i] > 0 && RectDirButton[i] === "Down") {
                                                    //scrh / 2 + i번째 ryd 값이 scrh - scrh /4 - (scrh을 32로 나눈 나머지)/2보다 작다면
                                                    if (scrh / 2 + ryd[i] < scrh - scrh / 4 - scrh % 32 / 2) {
                                                        //i번째 ryd값은 scrh/32만큼 증가
                                                        ryd[i] += (scrh / 32).toFloat()
                                                    }
                                                }
                                                //i번째 생명이 남아있고, i번째 방향이 위쪽이라면
                                                if (life[i] > 0 && RectDirButton[i] === "Up") {
                                                    //scrh / 2 + i번째 ryd 값이 (scrh을 32로 나눈 나머지)/2보다 크다면
                                                    if (scrh / 2 + ryd[i] > scrh % 32 / 2) {
                                                        //i번째 ryd값은 scrh/32만큼 감소
                                                        ryd[i] -= (scrh / 32).toFloat()
                                                    }
                                                }
                                                //i번째 생명이 남아있고, i번째 방향이 왼쪽이라면
                                                if (life[i] > 0 && RectDirButton[i] === "Left") {
                                                    //scrw / 2 + i번째 rxd 값이 (scrw을 64로 나눈 나머지)/2보다 크다면
                                                    if (scrw / 2 + rxd[i] > scrw % 64 / 2) {
                                                        //i번째 rxd값은 scrh/64만큼 감소
                                                        rxd[i] -= (scrw / 64).toFloat()
                                                    }
                                                }
                                                //i번째 생명이 남아있고, i번째 방향이 오른쪽이라면
                                                if (life[i] > 0 && RectDirButton[i] === "Right") {
                                                    //scrw / 2 + i번째 rxd 값이 scrw - scrw /8 - (scrw을 64로 나눈 나머지)/2보다 작다면
                                                    if (scrw / 2 + rxd[i] < scrw - scrw / 8 - scrw % 64 / 2) {
                                                        //i번째 rxd값은 scrh/64만큼 증가
                                                        rxd[i] += (scrw / 64).toFloat()
                                                    }
                                                }*/
                    }

                    //좌
                    //왼쪽 버튼을 클릭했거나 왼쪽으로 이동중이라면
                    if (start == true && DirButton == "Left" && count < 8
                        || start == false && count > 0 && count < 8 && DirButton == "Left"
                    ) {
                        //scrw / 2 + xd가 (scrw를 64로 나눈 나머지)/2 보다 크다면
                        if (scrw / 2 + xd > (scrw % 64) / 2) {
                            //count를 4로 나눈 나머지가 0이라면
                            if (count % 4 == 0) {
                                //xd값을 scrw/64만큼 감소
                                xd -= (scrw / 64).toFloat()
                                n = 1
                                //MD는 1
                                MD = 1
                                //count를 4로 나눈 나머지가 1 또는 3이라면
                            } else if (count % 4 == 1 || count % 4 == 3) {
                                //xd값을 scrw/64만큼 감소
                                xd -= (scrw / 64).toFloat()
                                n = 2
                                //count를 4로 나눈 나머지가 2라면
                            } else if (count % 4 == 2) {
                                //xd값을 scrw/64만큼 감소
                                xd -= (scrw / 64).toFloat()
                                n = 3
                            }
                        }
                    }

                    //우
                    //오른쪽 버튼을 클릭했거나 오른쪽으로 이동중이라면
                    if (start == true && DirButton == "Right" && count < 8
                        || start == false && count > 0 && count < 8 && DirButton == "Right"
                    ) {
                        //scrw / 2 + xd가 scrw - scrw /8 - (scrw를 64로 나눈 나머지)/2 보다 작다면
                        if (scrw / 2 + xd < scrw - scrw / 8 - (scrw % 64) / 2) {
                            //count를 4로 나눈 나머지가 0이라면
                            if (count % 4 == 0) {
                                //xd값을 scrw/64만큼 증가
                                xd += (scrw / 64).toFloat()
                                n = 4
                                //MD는 2
                                MD = 2
                                //count를 4로 나눈 나머지가 1 또는 3이라면
                            } else if (count % 4 == 1 || count % 4 == 3) {
                                //xd값을 scrw/64만큼 증가
                                xd += (scrw / 64).toFloat()
                                n = 5
                                //count를 4로 나눈 나머지가 2라면
                            } else if (count % 4 == 2) {
                                //xd값을 scrw/64만큼 증가
                                xd += (scrw / 64).toFloat()
                                n = 6
                            }
                        }
                    }

                    //상
                    //위쪽 버튼을 클릭했거나 위쪽으로 이동중이라면
                    if (start == true && DirButton == "Up" && count < 8
                        || start == false && count > 0 && count < 8 && DirButton == "Up"
                    ) {
                        //scrh / 2 + yd가 (scrh를 32로 나눈 나머지)/2 보다 크다면
                        if (scrh / 2 + yd > (scrh % 32) / 2) {
                            //count를 4로 나눈 나머지가 0이라면
                            if (count % 4 == 0) {
                                //yd값을 scrh/32만큼 감소
                                yd -= (scrh / 32).toFloat()
                                n = 7
                                //MD는 3
                                MD = 3
                                //count를 4로 나눈 나머지가 1 또는 3이라면
                            } else if (count % 4 == 1 || count % 4 == 3) {
                                //yd값을 scrh/32만큼 감소
                                yd -= (scrh / 32).toFloat()
                                n = 8
                                //count를 4로 나눈 나머지가 2라면
                            } else if (count % 4 == 2) {
                                //yd값을 scrh/32만큼 감소
                                yd -= (scrh / 32).toFloat()
                                n = 9
                            }
                        }
                    }

                    //하
                    if (start == true && DirButton == "Down" && count < 8
                        || start == false && count > 0 && count < 8 && DirButton == "Down"
                    ) {
                        //scrh / 2 + yd가 scrh - scrh /4 - (scrh를 32로 나눈 나머지)/2 보다 작다면
                        if (scrh / 2 + yd < scrh - scrh / 4 - (scrh % 32) / 2) {
                            //count를 4로 나눈 나머지가 0이라면
                            if (count % 4 == 0) {
                                //yd값을 scrh/32만큼 증가
                                yd += (scrh / 32).toFloat()
                                n = 10
                                //MD는 4
                                MD = 4
                                //count를 4로 나눈 나머지가 1 또는 3이라면
                            } else if (count % 4 == 1 || count % 4 == 3) {
                                //yd값을 scrh/32만큼 증가
                                yd += (scrh / 32).toFloat()
                                n = 11
                                //count를 4로 나눈 나머지가 2라면
                            } else if (count % 4 == 2) {
                                //yd값을 scrh/32만큼 증가
                                yd += (scrh / 32).toFloat()
                                n = 12
                            }
                        }
                    }

                    /*                    //i는 0부터 3보다 작은 동안 1씩 증가한다.
                                        for (i in 0..2) {
                                            //적군의 생명이 0보다 크다면
                                            if (life[i] > 0) {
                                                //카운트 수를 1씩 증가시킨다.
                                                count2[i] += 1
                                            }
                                        }*/

                    //0.1초 지연한다.
                    sleep(50)
                    //예외 사항
                } catch (e: Exception) {
                }
            }
        }
    }
}