package com.example.poyh

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val cardMovies = findViewById<MaterialCardView>(R.id.cardMovies)
        val cardAnimals = findViewById<MaterialCardView>(R.id.cardAnimals)
        val cardCelebrities = findViewById<MaterialCardView>(R.id.cardCelebrities)
        val cardActions = findViewById<MaterialCardView>(R.id.cardActions)

        cardMovies.setOnClickListener { onCategorySelected("Movies & Series") }
        cardAnimals.setOnClickListener { onCategorySelected("Animals & Nature") }
        cardCelebrities.setOnClickListener { onCategorySelected("Famous People") }
        cardActions.setOnClickListener { onCategorySelected("Act It Out") }
    }

    private fun onCategorySelected(categoryName: String) {
        Toast.makeText(this, "Selected: $categoryName", Toast.LENGTH_SHORT).show()
    }
}
