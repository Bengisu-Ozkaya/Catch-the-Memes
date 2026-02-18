package com.bngs0.catchthememes

import android.R.attr.start
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bngs0.catchthememes.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var imgArray = ArrayList<ImageView>()
    lateinit var timer: CountDownTimer
    var score = 0
    var randomNumber = 0
    var restart = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageView2.visibility = View.INVISIBLE
        binding.imageView3.visibility = View.INVISIBLE
        binding.imageView4.visibility = View.INVISIBLE
        binding.imageView5.visibility = View.INVISIBLE
        binding.imageView6.visibility = View.INVISIBLE
        binding.imageView7.visibility = View.INVISIBLE
        binding.imageView8.visibility = View.INVISIBLE
        binding.imageView9.visibility = View.INVISIBLE
        binding.imageView10.visibility = View.INVISIBLE

        imgArray.add(binding.imageView2)
        imgArray.add(binding.imageView4)
        imgArray.add(binding.imageView3)
        imgArray.add(binding.imageView5)
        imgArray.add(binding.imageView6)
        imgArray.add(binding.imageView7)
        imgArray.add(binding.imageView8)
        imgArray.add(binding.imageView9)
        imgArray.add(binding.imageView10)
    }

    fun clickImg(view: View){
        score++
        binding.scoreText.text = "Score: ${score}"
        view.visibility = View.INVISIBLE
    }

    fun startGame(view : View){
        binding.startButton.visibility = View.INVISIBLE
        if (restart){
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Do you want restart game?")
            alert.setMessage("Are you sure you want to restart the game?")
            alert.setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    score = 0;
                    binding.timeText.text = "Time: 0"
                    binding.scoreText.text = "Score: 0"
                    restart = false
                    binding.startButton.text = "Start Game"
                    binding.startButton.visibility = View.VISIBLE
                }
            })
            alert.setNegativeButton("No",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    TODO("Not yet implemented")
                }
            })
            alert.show()

        }else {
            timer = object : CountDownTimer(11000,1000){
                override fun onFinish() {
                    restart = true
                    binding.timeText.text = "Time: 0"
                    binding.startButton.text = "Restart Game"
                    binding.startButton.visibility = View.VISIBLE
                }

                override fun onTick(millisUntilFinished: Long) {
                    binding.timeText.text = "Time: ${millisUntilFinished/1000}"

                    randomNumber = (0..imgArray.size-1).random()
                    imgArray[randomNumber].visibility = View.VISIBLE
                }
            }
            timer.start()

        }
    }
}
