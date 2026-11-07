package com.example.poyh

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject

class CustomCategoryActivity : AppCompatActivity() {

    private val wordsList = mutableListOf<String>()
    private lateinit var adapter: CustomWordAdapter
    private lateinit var tvWordCount: TextView
    private lateinit var etWordInput: TextInputEditText
    private lateinit var etDeckName: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_category)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        etDeckName = findViewById(R.id.etDeckName)
        etWordInput = findViewById(R.id.etWordInput)
        tvWordCount = findViewById(R.id.tvWordCount)
        val btnAddWord = findViewById<MaterialButton>(R.id.btnAddWord)
        val btnSaveCategory = findViewById<MaterialButton>(R.id.btnSaveCategory)
        val rvCustomWords = findViewById<RecyclerView>(R.id.rvCustomWords)

        adapter = CustomWordAdapter(wordsList) { position ->
            wordsList.removeAt(position)
            adapter.notifyItemRemoved(position)
            updateWordCount()
        }

        rvCustomWords.layoutManager = LinearLayoutManager(this)
        rvCustomWords.adapter = adapter

        btnAddWord.setOnClickListener {
            addWordFromInput()
        }

        etWordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWordFromInput()
                true
            } else {
                false
            }
        }

        btnSaveCategory.setOnClickListener {
            saveCategoryAndFinish()
        }

        updateWordCount()
    }

    private fun addWordFromInput() {
        val word = etWordInput.text?.toString()?.trim().orEmpty()
        if (word.isEmpty()) {
            Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show()
            return
        }

        wordsList.add(word)
        adapter.notifyItemInserted(wordsList.size - 1)
        etWordInput.text?.clear()
        updateWordCount()
    }

    private fun updateWordCount() {
        tvWordCount.text = "Words (${wordsList.size})"
    }

    private fun saveCategoryAndFinish() {
        val deckName = etDeckName.text?.toString()?.trim().orEmpty()
        if (deckName.isEmpty()) {
            Toast.makeText(this, "Please enter a deck name", Toast.LENGTH_SHORT).show()
            return
        }

        if (wordsList.isEmpty()) {
            Toast.makeText(this, "Please add at least one word", Toast.LENGTH_SHORT).show()
            return
        }

        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val rawJson = prefs.getString(KEY_CUSTOM_CATEGORIES, "[]") ?: "[]"
        val jsonArray = try {
            JSONArray(rawJson)
        } catch (e: Exception) {
            JSONArray()
        }

        val newCategoryObj = JSONObject().apply {
            put("id", "custom_${System.currentTimeMillis()}")
            put("name", deckName)
            val wordsArray = JSONArray()
            for (word in wordsList) {
                wordsArray.put(word)
            }
            put("words", wordsArray)
        }

        jsonArray.put(newCategoryObj)

        prefs.edit().putString(KEY_CUSTOM_CATEGORIES, jsonArray.toString()).apply()

        Toast.makeText(this, "Category saved successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val PREFS_NAME = "poyh_custom_categories"
        const val KEY_CUSTOM_CATEGORIES = "custom_categories_json"
    }
}
