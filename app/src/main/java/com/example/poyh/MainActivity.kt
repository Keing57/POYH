package com.example.poyh

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
            Toast.makeText(this, "Starting Game...", Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this, "Opening Settings...", Toast.LENGTH_SHORT).show()
        }
    }
}
