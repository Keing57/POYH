package com.example.poyh

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

        val categories = listOf(
            Category("animals", "Animals", "🦁", "Wild beasts, pets, and sea creatures"),
            Category("movies", "Movies", "🎬", "Hollywood hits, blockbusters, and classics"),
            Category("jobs", "Jobs", "💼", "Professions, careers, and weird occupations"),
            Category("celebrities", "Celebrities", "⭐", "Actors, singers, athletes, and icons"),
            Category("act_it_out", "Act It Out", "🎭", "Fun gestures, mimes, and charades"),
            Category("food", "Food & Drink", "🍕", "Delicious meals, snacks, and beverages"),
            Category("gaming", "Video Games", "🎮", "Famous games, characters, and consoles")
        )

        rvCategories.adapter = CategoryAdapter(categories) { category ->
            Toast.makeText(this, "Selected: ${category.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
