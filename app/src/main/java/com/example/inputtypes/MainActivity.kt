package com.example.inputtypes

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private lateinit var textView: TextView

    override fun onStart() {
        super.onStart()
        textView = findViewById<TextView>(R.id.textContainer)
        startAnimation()
    }

    private fun startAnimation(){
        /*textView.startAnimation(animation)*/
        val sentence = "This is your sentence"
        ValueAnimator.ofInt(0, sentence.length - 1).apply {
            var index = -1
            interpolator = LinearInterpolator()
            duration = 3000
            addUpdateListener {
                val currentIndexChar = it.animatedValue as Int
                if(index != currentIndexChar){
                    val currentChar = sentence[currentIndexChar]
                    textView.text = "${textView.text}$currentChar"
                }
                index = currentIndexChar
            }
        }.start()
    }
}