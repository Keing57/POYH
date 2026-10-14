package com.example.poyh

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.abs

class GameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var rootLayout: View
    private lateinit var tvWord: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvGameCategory: TextView

    private var wordsList: MutableList<String> = mutableListOf()
    private var currentWordIndex = 0
    private var score: Int = 0

    // Record words and whether guessed or passed
    private val wordResultsList: ArrayList<WordResult> = ArrayList()

    private var categoryId: String = "animals"
    private var categoryName: String = "Animals"

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    // Cooldown & debounce variables for tilt detection
    private var lastActionTime: Long = 0L
    private val actionCooldownMs: Long = 1500L
    private var hasReturnedToNeutral: Boolean = true

    // Visual feedback handler
    private val feedbackHandler = Handler(Looper.getMainLooper())
    private var resetBackgroundRunnable: Runnable? = null

    // Timers & game status
    private var getReadyTimer: CountDownTimer? = null
    private var countDownTimer: CountDownTimer? = null
    private var isGameActive: Boolean = false
    private var isGameOver: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        rootLayout = findViewById(R.id.gameRootLayout)
        tvWord = findViewById(R.id.tvWord)
        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvGameCategory = findViewById(R.id.tvGameCategory)

        // 1. Read received category
        categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "animals"
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: categoryId.replaceFirstChar { it.uppercase() }
        tvGameCategory.text = categoryName

        // 2. Fetch words from repository and shuffle
        val rawWords = WordRepository.getWords(categoryId)
        wordsList = rawWords.shuffled().toMutableList()

        // 3. Initial stats
        score = 0
        tvScore.text = score.toString()
        tvTimer.text = "60s"

        // 4. Initialize SensorManager and Accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // 5. Start 3.. 2.. 1.. Get Ready countdown before starting main game
        startGetReadyCountdown()
    }

    private fun startGetReadyCountdown() {
        isGameActive = false
        tvWord.text = "3"

        getReadyTimer?.cancel()
        getReadyTimer = object : CountDownTimer(3500L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000L).toInt()
                if (seconds in 1..3) {
                    tvWord.text = seconds.toString()
                }
            }

            override fun onFinish() {
                // Countdown finished: display first word and activate game
                isGameActive = true
                if (wordsList.isNotEmpty()) {
                    currentWordIndex = 0
                    tvWord.text = wordsList[currentWordIndex]
                } else {
                    tvWord.text = "No words"
                }

                // Start main 60-second game timer
                startCountdownTimer()
            }
        }.start()
    }

    private fun startCountdownTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished + 500L) / 1000L
                tvTimer.text = "${secondsRemaining}s"
            }

            override fun onFinish() {
                tvTimer.text = "0s"
                tvWord.text = "TIME'S UP!"
                isGameActive = false
                isGameOver = true

                // Block sensor input
                sensorManager?.unregisterListener(this@GameActivity)

                // Wait 2 seconds so user sees "TIME'S UP!", then transition to ResultActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!isFinishing) {
                        val intent = Intent(this@GameActivity, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.EXTRA_SCORE, score)
                            putExtra(ResultActivity.EXTRA_CATEGORY_ID, categoryId)
                            putExtra(ResultActivity.EXTRA_CATEGORY_NAME, categoryName)
                            putExtra(ResultActivity.EXTRA_WORD_RESULTS, wordResultsList)
                        }
                        startActivity(intent)
                        finish()
                    }
                }, 2000L)
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        if (!isGameOver) {
            accelerometer?.let { sensor ->
                sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
        resetBackgroundRunnable?.let { feedbackHandler.removeCallbacks(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        getReadyTimer?.cancel()
        countDownTimer?.cancel()
        resetBackgroundRunnable?.let { feedbackHandler.removeCallbacks(it) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isGameActive || isGameOver || event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

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
        if (!isGameActive || isGameOver) return
        score++
        tvScore.text = score.toString()

        flashScreenFeedback(R.color.feedback_correct)

        if (wordsList.isNotEmpty()) {
            wordResultsList.add(WordResult(wordsList[currentWordIndex], isGuessed = true))
            currentWordIndex++
            if (currentWordIndex >= wordsList.size) {
                currentWordIndex = 0
            }
            tvWord.text = wordsList[currentWordIndex]
        }
    }

    private fun handlePass() {
        if (!isGameActive || isGameOver) return
        flashScreenFeedback(R.color.feedback_pass)

        if (wordsList.isNotEmpty()) {
            wordResultsList.add(WordResult(wordsList[currentWordIndex], isGuessed = false))
            currentWordIndex++
            if (currentWordIndex >= wordsList.size) {
                currentWordIndex = 0
            }
            tvWord.text = wordsList[currentWordIndex]
        }
    }

    private fun flashScreenFeedback(colorResId: Int) {
        resetBackgroundRunnable?.let { feedbackHandler.removeCallbacks(it) }
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, colorResId))

        resetBackgroundRunnable = Runnable {
            rootLayout.setBackgroundResource(R.drawable.bg_main_gradient)
        }
        feedbackHandler.postDelayed(resetBackgroundRunnable!!, 1000L)
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
