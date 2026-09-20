package com.example.poyh

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<MaterialButton>(R.id.btnPlay)
        val btnSettings = findViewById<MaterialButton>(R.id.btnSettings)

        btnPlay.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this, "Opening Settings...", Toast.LENGTH_SHORT).show()
        }
    }
}
