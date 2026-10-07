package com.example.poyh

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvScore = findViewById<TextView>(R.id.tvScore)
        val btnPlayAgain = findViewById<MaterialButton>(R.id.btnPlayAgain)
        val btnMainMenu = findViewById<MaterialButton>(R.id.btnMainMenu)

        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        val categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "animals"
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: "Animals"

        // Display: "Your Score: X"
        tvScore.text = "Your Score: $score"

        btnPlayAgain.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_CATEGORY_ID, categoryId)
                putExtra(GameActivity.EXTRA_CATEGORY_NAME, categoryName)
            }
            startActivity(intent)
            finish()
        }

        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
