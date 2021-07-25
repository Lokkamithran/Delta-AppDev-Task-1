package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity : AppCompatActivity() {

    private var score: Int? = null
    private var highScore: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        supportActionBar?.hide()
        setContentView(R.layout.activity_third)

        val scoreText = findViewById<TextView>(R.id.scoreText)
        val goBackButton = findViewById<Button>(R.id.goBackButton)
        score = intent.getIntExtra("score", 0)

        scoreText.text = getString(R.string.scoreText, score)
        goBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        loadData()
        if(score!! > highScore!!) saveData()
    }
    private fun saveData(){
        val sharedPreferences= getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        score?.let { editor.putInt("score", it) }
        editor.apply()
    }
    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        highScore = sharedPreferences.getInt("score", 0)
    }
}