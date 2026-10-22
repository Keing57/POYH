package com.example.poyh

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var switchSound: SwitchMaterial
    private lateinit var switchVibration: SwitchMaterial
    private lateinit var rgRoundDuration: RadioGroup
    private lateinit var rb60s: RadioButton
    private lateinit var rb90s: RadioButton
    private lateinit var rb120s: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnBack = findViewById(R.id.btnBack)
        switchSound = findViewById(R.id.switchSound)
        switchVibration = findViewById(R.id.switchVibration)
        rgRoundDuration = findViewById(R.id.rgRoundDuration)
        rb60s = findViewById(R.id.rb60s)
        rb90s = findViewById(R.id.rb90s)
        rb120s = findViewById(R.id.rb120s)

        btnBack.setOnClickListener {
            finish()
        }
    }
}
