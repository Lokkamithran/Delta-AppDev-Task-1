package com.example.quiz

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SecondActivity : AppCompatActivity(), View.OnClickListener{

    private var option1: Button? = null
    private var option2: Button? = null
    private var option3: Button? = null
    private var option4: Button? = null
    private var question: TextView? = null
    private var timeRemaining: TextView? = null
    private var timer: Any? = null
    private var timeRunning = false
    private var scoreText: TextView? = null

    private var countdownMillis: Long = 60000

    var score = 0
    @RequiresApi(Build.VERSION_CODES.O)
    var date = Date().randomDate()
    @RequiresApi(Build.VERSION_CODES.O)
    var day = date.dayOfWeek.toString().lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    @RequiresApi(Build.VERSION_CODES.O)
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    var formattedDate: String = date.format(formatter)
    private var list =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").toMutableList()
        init{ list.remove(day) }
    private var shuffledList = list.shuffled()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0,0)
        supportActionBar?.hide()
        setContentView(R.layout.activity_second)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        question = findViewById(R.id.question)
        timeRemaining = findViewById(R.id.timeRemaining)
        scoreText = findViewById(R.id.scoreText)

        question?.text = formattedDate

        randomOption()
        option1?.setOnClickListener(this)
        option2?.setOnClickListener(this)
        option3?.setOnClickListener(this)
        option4?.setOnClickListener(this)

        if(savedInstanceState!=null){
            score = savedInstanceState.getInt("score")
            question?.text = savedInstanceState.getString("question")
            option1?.text = savedInstanceState.getString("option1")
            option2?.text = savedInstanceState.getString("option2")
            option3?.text = savedInstanceState.getString("option3")
            option4?.text = savedInstanceState.getString("option4")
            day = savedInstanceState.getString("day").toString()
            countdownMillis = savedInstanceState.getLong("timeRemaining")
            startTimer(countdownMillis)
        }
        if(!timeRunning) startTimer(countdownMillis)
        scoreText?.text = getString(R.string.inGameScore, score)
    }
    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("score", score)
        outState.putString("question", question?.text.toString())
        outState.putString("option1", option1?.text.toString())
        outState.putString("option2", option2?.text.toString())
        outState.putString("option3", option3?.text.toString())
        outState.putString("option4", option4?.text.toString())
        outState.putString("day", day)
        outState.putLong("timeRemaining", countdownMillis)
        stopTimer()
    }
    private fun randomOption() {
        when ((1..4).random()) {
            1 -> {
                assignOption(option1, "a")
            }
            2 -> {
                assignOption(option2, "b")
            }
            3 -> {
                assignOption(option3, "c")
            }
            4 -> {
                assignOption(option4, "d")
            }
        }
    }
    private fun assignOption(view: Button?, flag: String) {
        view?.text = day
        when (flag) {
            "a" -> {
                option2?.text = shuffledList[0]
                option3?.text = shuffledList[1]
                option4?.text = shuffledList[2]
            }
            "b" -> {
                option1?.text = shuffledList[0]
                option3?.text = shuffledList[1]
                option4?.text = shuffledList[2]
            }
            "c" -> {
                option2?.text = shuffledList[0]
                option1?.text = shuffledList[1]
                option4?.text = shuffledList[2]
            }
            "d" -> {
                option2?.text = shuffledList[0]
                option3?.text = shuffledList[1]
                option1?.text = shuffledList[2]
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        vibratePhone()
        val text = when(v!!.id){
            R.id.option1 -> option1?.text.toString()
            R.id.option2 -> option2?.text.toString()
            R.id.option3 -> option3?.text.toString()
            else -> option4?.text.toString()
        }
        if(text == day){
            score+=3
            scoreText?.text = getString(R.string.inGameScore, score)
            question?.background = ResourcesCompat.getDrawable(resources, R.drawable.backgreen, null)
            question?.setTextColor(resources.getColor(R.color.black))
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({question?.background = ResourcesCompat.getDrawable(resources, R.drawable.backnormal, null)
                question?.setTextColor(resources.getColor(R.color.white))},
            500)
            generateQuestion()
        }
        else{
            score-=1
            scoreText?.text = getString(R.string.inGameScore, score)
            question?.background = ResourcesCompat.getDrawable(resources, R.drawable.backred, null)
            question?.setTextColor(resources.getColor(R.color.black))
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({question?.background = ResourcesCompat.getDrawable(resources, R.drawable.backnormal, null)
                question?.setTextColor(resources.getColor(R.color.white))},
                500)
            stopTimer()
            countdownMillis=((countdownMillis/1000)-5)*1000
            startTimer(countdownMillis)
            generateQuestion()
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateQuestion(){
        date = Date().randomDate()
        day = date.dayOfWeek.toString().lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        formattedDate = date.format(formatter)
        question?.text = formattedDate
        list = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").toMutableList()
        list.remove(day)
        shuffledList = list.shuffled()
        randomOption()
    }
    private fun startTimer(timeInMillis: Long){
        timer = object: CountDownTimer(timeInMillis, 1000){
            override fun onTick(millisUntilFinished: Long) {
                countdownMillis = millisUntilFinished
                timeRemaining?.text = getString(R.string.timeRemaining,(countdownMillis/1000).toInt())
            }
            override fun onFinish() {
                val intent = Intent(this@SecondActivity, ThirdActivity::class.java)
                intent.putExtra("score", score)
                startActivity(intent)
            }
        }
        (timer as CountDownTimer).start()
        timeRunning = true
    }
    private fun stopTimer(){
        (timer as CountDownTimer).cancel()
        timeRunning = false
    }
}
class Date{
    private val year = (1900..2000).random()
    private val month = (1..12).random()
    private val date = (1..28).random()
    @RequiresApi(Build.VERSION_CODES.O)
    fun randomDate(): LocalDate {
        val fullDate = LocalDate.of(year, month, date)
        return(fullDate)
    }
}