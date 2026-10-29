package com.example.poyh

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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

    private val wordResultsList: ArrayList<WordResult> = ArrayList()

    private var categoryId: String = "animals"
    private var categoryName: String = "Animals"

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var lastActionTime: Long = 0L
    private val actionCooldownMs: Long = 1500L
    private var hasReturnedToNeutral: Boolean = true

    private val feedbackHandler = Handler(Looper.getMainLooper())
    private var resetBackgroundRunnable: Runnable? = null

    private var getReadyTimer: CountDownTimer? = null
    private var countDownTimer: CountDownTimer? = null
    private var isGameActive: Boolean = false
    private var isGameOver: Boolean = false

    private var soundPool: SoundPool? = null
    private var soundDingId: Int = 0
    private var soundBuzzId: Int = 0
    private var soundTickId: Int = 0

    private var vibrator: Vibrator? = null

    private var isSoundEnabled: Boolean = true
    private var isVibrationEnabled: Boolean = true
    private var roundDuration: Int = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        loadPreferences()

        rootLayout = findViewById(R.id.gameRootLayout)
        tvWord = findViewById(R.id.tvWord)
        tvTimer = findViewById(R.id.tvTimer)
        tvScore = findViewById(R.id.tvScore)
        tvGameCategory = findViewById(R.id.tvGameCategory)

        categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "animals"
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: categoryId.replaceFirstChar { it.uppercase() }
        tvGameCategory.text = categoryName

        val rawWords = WordRepository.getWords(categoryId)
        wordsList = rawWords.shuffled().toMutableList()

        score = 0
        tvScore.text = score.toString()
        tvTimer.text = "${roundDuration}s"

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        initVibrator()
        initSoundPool()

        startGetReadyCountdown()
    }

    private fun loadPreferences() {
        val prefs: SharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        isSoundEnabled = prefs.getBoolean(SettingsActivity.KEY_SOUND_ENABLED, true)
        isVibrationEnabled = prefs.getBoolean(SettingsActivity.KEY_VIBRATION_ENABLED, true)
        roundDuration = prefs.getInt(SettingsActivity.KEY_ROUND_DURATION, 60)
    }

    private fun initVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun vibrateCorrect() {
        if (!isVibrationEnabled) return
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100L)
            }
        }
    }

    private fun vibratePass() {
        if (!isVibrationEnabled) return
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(400L, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(400L)
            }
        }
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        soundDingId = soundPool?.load(this, R.raw.ding, 1) ?: 0
        soundBuzzId = soundPool?.load(this, R.raw.buzz, 1) ?: 0
        soundTickId = soundPool?.load(this, R.raw.tick, 1) ?: 0
    }

    private fun playSoundDing() {
        if (!isSoundEnabled) return
        if (soundDingId != 0) {
            soundPool?.play(soundDingId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    private fun playSoundBuzz() {
        if (!isSoundEnabled) return
        if (soundBuzzId != 0) {
            soundPool?.play(soundBuzzId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    private fun playSoundTick() {
        if (!isSoundEnabled) return
        if (soundTickId != 0) {
            soundPool?.play(soundTickId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
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
                isGameActive = true
                if (wordsList.isNotEmpty()) {
                    currentWordIndex = 0
                    tvWord.text = wordsList[currentWordIndex]
                } else {
                    tvWord.text = "No words"
                }

                startCountdownTimer()
            }
        }.start()
    }

    private fun startCountdownTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer((roundDuration * 1000).toLong(), 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished + 500L) / 1000L
                tvTimer.text = "${secondsRemaining}s"
                if (secondsRemaining in 1..10) {
                    playSoundTick()
                }
            }

            override fun onFinish() {
                tvTimer.text = "0s"
                tvWord.text = "TIME'S UP!"
                isGameActive = false
                isGameOver = true

                sensorManager?.unregisterListener(this@GameActivity)

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
        soundPool?.release()
        soundPool = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isGameActive || isGameOver || event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val z = event.values[2]
        val currentTime = System.currentTimeMillis()

        if (abs(z) < 3.5f) {
            hasReturnedToNeutral = true
        }

        if (hasReturnedToNeutral && (currentTime - lastActionTime > actionCooldownMs)) {
            if (z > 7.0f) {
                Log.d(TAG, "CORRECT")
                lastActionTime = currentTime
                hasReturnedToNeutral = false
                handleCorrect()
            } else if (z < -7.0f) {
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

        vibrateCorrect()
        playSoundDing()
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

        vibratePass()
        playSoundBuzz()
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
    }

    companion object {
        const val TAG = "POYH_GAME"
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
