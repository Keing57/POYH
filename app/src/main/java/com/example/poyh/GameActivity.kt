package com.example.poyh

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvWord: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvGameCategory: TextView

    private var wordsList: MutableList<String> = mutableListOf()
    private var currentWordIndex = 0

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

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
        tvScore.text = "0"
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
        // Accelerometer sensor event values will be processed here
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if necessary
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
