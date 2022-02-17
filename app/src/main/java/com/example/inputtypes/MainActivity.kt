package com.example.inputtypes

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inputtypes.input.currency.CurrencyInputDouble

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private lateinit var textView: TextView
    private lateinit var editText: CurrencyInputDouble
    private lateinit var shakeAnimation: Animation

    override fun onStart() {
        super.onStart()
        textView = findViewById<TextView>(R.id.textContainer)
        editText = findViewById(R.id.currencyEdit)
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.falling_text)
        shakeInput()
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

    private fun shakeInput(){
        editText.onInputChanged = {
            textView.text = it
            textView.startAnimation(shakeAnimation)
        }
    }
}
