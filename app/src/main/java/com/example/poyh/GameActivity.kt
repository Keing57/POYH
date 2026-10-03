package com.example.poyh

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvWord: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvGameCategory: TextView

    private var wordsList: MutableList<String> = mutableListOf()
    private var currentWordIndex = 0
    private var score: Int = 0

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    // Cooldown & debounce variables for tilt detection
    private var lastActionTime: Long = 0L
    private val actionCooldownMs: Long = 1500L
    private var hasReturnedToNeutral: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvWord = findViewById(R.id.tvWord)
        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvGameCategory = findViewById(R.id.tvGameCategory)

        // 1. Read received category
        val categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "animals"
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: categoryId.replaceFirstChar { it.uppercase() }
        tvGameCategory.text = categoryName

        // 2. Fetch words from repository and shuffle
        val rawWords = WordRepository.getWords(categoryId)
        wordsList = rawWords.shuffled().toMutableList()

        // 3. Initial stats
        score = 0
        tvScore.text = score.toString()
        tvTimer.text = "60s"

        // 4. Display first word
        if (wordsList.isNotEmpty()) {
            currentWordIndex = 0
            tvWord.text = wordsList[currentWordIndex]
        } else {
            tvWord.text = "No words"
        }

        // 5. Initialize SensorManager and Accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val z = event.values[2]
        val currentTime = System.currentTimeMillis()

        // Reset to neutral when device is upright (|z| < 3.5)
        if (abs(z) < 3.5f) {
            hasReturnedToNeutral = true
        }

        // Detect tilt only if cooldown has passed and device returned to neutral position
        if (hasReturnedToNeutral && (currentTime - lastActionTime > actionCooldownMs)) {
            if (z > 7.0f) {
                // Tilted forward (screen pointing down) -> CORRECT
                Log.d(TAG, "CORRECT")
                lastActionTime = currentTime
                hasReturnedToNeutral = false
                handleCorrect()
            } else if (z < -7.0f) {
                // Tilted backward (screen pointing up) -> PASS
                Log.d(TAG, "PASS")
                lastActionTime = currentTime
                hasReturnedToNeutral = false
                handlePass()
            }
        }
    }

    private fun handleCorrect() {
        score++
        tvScore.text = score.toString()

        if (wordsList.isNotEmpty()) {
            currentWordIndex++
            if (currentWordIndex >= wordsList.size) {
                currentWordIndex = 0
            }
            tvWord.text = wordsList[currentWordIndex]
        }
    }

    private fun handlePass() {
        if (wordsList.isNotEmpty()) {
            currentWordIndex++
            if (currentWordIndex >= wordsList.size) {
                currentWordIndex = 0
            }
            tvWord.text = wordsList[currentWordIndex]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    companion object {
        const val TAG = "POYH_GAME"
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
