package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var highScore: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.start)
        val quitButton = findViewById<Button>(R.id.quit)
        val highScoreText = findViewById<TextView>(R.id.highScore)

        quitButton.setOnClickListener { finishAffinity() }
        startButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        loadData()
        highScoreText.text = getString(R.string.highScore, highScore)
    }
    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        highScore = sharedPreferences.getInt("score", 0)
    }
}