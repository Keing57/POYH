package com.example.poyh

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
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

        // Expanded list of categories with descriptive vector icons
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
            ),
            Category(
                id = "brands",
                name = "Brands",
                iconResId = R.drawable.ic_brands,
                backgroundColor = Color.parseColor("#FD79A8")
            ),
            Category(
                id = "singers",
                name = "Singers",
                iconResId = R.drawable.ic_singers,
                backgroundColor = Color.parseColor("#FDCB6E")
            ),
            Category(
                id = "countries",
                name = "Countries",
                iconResId = R.drawable.ic_countries,
                backgroundColor = Color.parseColor("#0984E3")
            )
        )

        rvCategories.adapter = CategoryAdapter(categories) { selectedCategory ->
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_CATEGORY_ID, selectedCategory.id)
                putExtra(GameActivity.EXTRA_CATEGORY_NAME, selectedCategory.name)
            }
            startActivity(intent)
        }
    }
}
