package com.example.poyh

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var tvWord: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvGameCategory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvWord = findViewById(R.id.tvWord)
        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvGameCategory = findViewById(R.id.tvGameCategory)

        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: "ANIMALS"
        tvGameCategory.text = categoryName

        // Initial default values
        tvScore.text = "0"
        tvTimer.text = "60s"
        tvWord.text = "READY?"
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
