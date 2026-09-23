package com.example.poyh

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)

        // Hardcoded list of 3 categories
        val categories = listOf(
            Category(
                id = "animals",
                name = "Animals",
                iconResId = R.drawable.ic_animals,
                backgroundColor = Color.parseColor("#6C5CE7")
            ),
            Category(
                id = "movies",
                name = "Movies",
                iconResId = R.drawable.ic_movies,
                backgroundColor = Color.parseColor("#00CEC9")
            ),
            Category(
                id = "jobs",
                name = "Jobs",
                iconResId = R.drawable.ic_jobs,
                backgroundColor = Color.parseColor("#FF7675")
            )
        )

        rvCategories.adapter = CategoryAdapter(categories) { selectedCategory ->
            Toast.makeText(
                this,
                "Category selected: ${selectedCategory.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
