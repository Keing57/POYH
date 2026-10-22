package com.example.poyh

import android.content.Context
import android.content.SharedPreferences
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

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        btnBack = findViewById(R.id.btnBack)
        switchSound = findViewById(R.id.switchSound)
        switchVibration = findViewById(R.id.switchVibration)
        rgRoundDuration = findViewById(R.id.rgRoundDuration)
        rb60s = findViewById(R.id.rb60s)
        rb90s = findViewById(R.id.rb90s)
        rb120s = findViewById(R.id.rb120s)

        loadSettings()

        btnBack.setOnClickListener {
            finish()
        }

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_SOUND_ENABLED, isChecked).apply()
        }

        switchVibration.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, isChecked).apply()
        }

        rgRoundDuration.setOnCheckedChangeListener { _, checkedId ->
            val duration = when (checkedId) {
                R.id.rb90s -> 90
                R.id.rb120s -> 120
                else -> 60
            }
            prefs.edit().putInt(KEY_ROUND_DURATION, duration).apply()
        }
    }

    private fun loadSettings() {
        val soundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        val vibrationEnabled = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        val roundDuration = prefs.getInt(KEY_ROUND_DURATION, 60)

        switchSound.isChecked = soundEnabled
        switchVibration.isChecked = vibrationEnabled

        when (roundDuration) {
            90 -> rb90s.isChecked = true
            120 -> rb120s.isChecked = true
            else -> rb60s.isChecked = true
        }
    }

    companion object {
        const val PREFS_NAME = "poyh_settings"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        const val KEY_ROUND_DURATION = "round_duration"
    }
}
