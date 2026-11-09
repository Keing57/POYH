package com.example.poyh

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvCategories: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val fabAddCategory = findViewById<FloatingActionButton>(R.id.fabAddCategory)
        fabAddCategory.setOnClickListener {
            val intent = Intent(this, CustomCategoryActivity::class.java)
            startActivity(intent)
        }

        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)

        updateCategoriesList()
    }

    override fun onResume() {
        super.onResume()
        updateCategoriesList()
    }

    private fun updateCategoriesList() {
        val categories = loadCategories()
        rvCategories.adapter = CategoryAdapter(categories) { selectedCategory ->
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_CATEGORY_ID, selectedCategory.id)
                putExtra(GameActivity.EXTRA_CATEGORY_NAME, selectedCategory.name)
                if (selectedCategory.customWords != null) {
                    putStringArrayListExtra(GameActivity.EXTRA_CUSTOM_WORDS, selectedCategory.customWords)
                }
            }
            startActivity(intent)
        }
    }

    private fun loadCategories(): List<Category> {
        val categories = mutableListOf(
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

        val customColors = listOf(
            Color.parseColor("#E17055"),
            Color.parseColor("#00B894"),
            Color.parseColor("#E84393"),
            Color.parseColor("#6C5CE7"),
            Color.parseColor("#0984E3")
        )

        val prefs = getSharedPreferences(CustomCategoryActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val rawJson = prefs.getString(CustomCategoryActivity.KEY_CUSTOM_CATEGORIES, null)
        if (!rawJson.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(rawJson)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val id = obj.optString("id", "custom_$i")
                    val name = obj.optString("name", "Custom Deck")
                    val wordsJson = obj.optJSONArray("words")
                    val wordsList = ArrayList<String>()
                    if (wordsJson != null) {
                        for (j in 0 until wordsJson.length()) {
                            wordsList.add(wordsJson.getString(j))
                        }
                    }
                    val color = customColors[i % customColors.size]
                    categories.add(
                        Category(
                            id = id,
                            name = name,
                            iconResId = R.drawable.ic_custom,
                            backgroundColor = color,
                            customWords = wordsList
                        )
                    )
                }
            } catch (e: Exception) {
            }
        }

        return categories
    }
}
