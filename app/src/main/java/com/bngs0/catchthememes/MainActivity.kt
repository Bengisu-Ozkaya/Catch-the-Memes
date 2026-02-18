package com.bngs0.catchthememes

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bngs0.catchthememes.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imgArray = ArrayList<ImageView>()
    private var handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable { }
    private lateinit var countDownTimer: CountDownTimer

    var score = 0
    var interval = 1000L // başlangıç hızı
    var gameRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imgArray.add(binding.imageView2)
        imgArray.add(binding.imageView3)
        imgArray.add(binding.imageView4)
        imgArray.add(binding.imageView5)
        imgArray.add(binding.imageView6)
        imgArray.add(binding.imageView7)
        imgArray.add(binding.imageView8)
        imgArray.add(binding.imageView9)
        imgArray.add(binding.imageView10)

        hideAllImages()
    }

    fun startGame(view: View) {
        if (gameRunning) {
            showRestartDialog()
            return
        }
        gameRunning = true
        score = 0
        interval = 1000L
        binding.scoreText.text = "Score: 0"
        binding.startButton.visibility = View.INVISIBLE

        startCountDown()
        startImageLoop()
    }

    fun clickImg(view: View) {
        if (!gameRunning) return
        score++
        binding.scoreText.text = "Score: $score"
        view.visibility = View.INVISIBLE

        // Her 5 puanda hız artar
        if (score % 5 == 0) {
            interval = (interval * 0.75).toLong()
            if (interval < 200L) interval = 200L
            restartImageLoop() // yeni hızla yeniden başlat
        }
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timeText.text = "Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                binding.timeText.text = "Time: 0"
                endGame()
            }
        }.start()
    }

    private fun startImageLoop() {
        runnable = object : Runnable {
            override fun run() {
                hideAllImages()
                val randomIndex = Random().nextInt(imgArray.size)
                imgArray[randomIndex].visibility = View.VISIBLE
                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }

    private fun restartImageLoop() {
        handler.removeCallbacks(runnable)
        startImageLoop()
    }

    private fun endGame() {
        gameRunning = false
        handler.removeCallbacks(runnable)
        hideAllImages()

        val prefs = getSharedPreferences("package com.bngs0.catchthememes",MODE_PRIVATE)
        val bestScore = prefs.getInt("best", 0)
        if (score > bestScore) {
            prefs.edit().putInt("best", score).apply()
        }
        val updatedBestScore = prefs.getInt("best", 0)

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Time Out! Score: $score")
        alert.setMessage("Your Best Score: $updatedBestScore\n\nDo you want to learn more memes looser?")
        alert.setPositiveButton("Yes") { _, _ ->
            score = 0
            interval = 1000L
            binding.timeText.text = "Time: 15"
            binding.scoreText.text = "Score: 0"
            binding.startButton.text = "Start Game"
            binding.startButton.visibility = View.VISIBLE
        }
        alert.setNegativeButton("No") { _, _ ->
            Toast.makeText(this, "HAHA! Are you giving up already loser?", Toast.LENGTH_LONG).show()
        }
        alert.show()
    }

    private fun showRestartDialog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Time Out!")
        alert.setMessage("Do you want to learn more memes looser?")
        alert.setPositiveButton("Yes") { _, _ ->
            countDownTimer.cancel()
            handler.removeCallbacks(runnable)
            gameRunning = false
            score = 0
            interval = 1000L
            binding.timeText.text = "Time: 15"
            binding.scoreText.text = "Score: 0"
            binding.startButton.visibility = View.VISIBLE
            hideAllImages()
        }
        alert.setNegativeButton("No", null)
        alert.show()
    }

    private fun hideAllImages() {
        for (img in imgArray) {
            img.visibility = View.INVISIBLE
        }
    }
}