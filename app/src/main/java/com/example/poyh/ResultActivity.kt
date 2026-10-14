package com.example.poyh

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvScore = findViewById<TextView>(R.id.tvScore)
        val btnPlayAgain = findViewById<MaterialButton>(R.id.btnPlayAgain)
        val btnMainMenu = findViewById<MaterialButton>(R.id.btnMainMenu)
        val rvWordResults = findViewById<RecyclerView>(R.id.rvWordResults)

        val score = intent.getIntExtra(EXTRA_SCORE, 0)

        // Display: "Your Score: X"
        tvScore.text = "Your Score: $score"

        // Read and populate word results
        @Suppress("UNCHECKED_CAST")
        val wordResults = (intent.getSerializableExtra(EXTRA_WORD_RESULTS) as? ArrayList<WordResult>) ?: arrayListOf()

        rvWordResults.layoutManager = LinearLayoutManager(this)
        rvWordResults.adapter = WordResultAdapter(wordResults)

        // "Play Again" navigates back to CategoryActivity
        btnPlayAgain.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // "Main Menu" navigates back to MainActivity clearing the entire Activity stack
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
        const val EXTRA_WORD_RESULTS = "extra_word_results"
    }
}
