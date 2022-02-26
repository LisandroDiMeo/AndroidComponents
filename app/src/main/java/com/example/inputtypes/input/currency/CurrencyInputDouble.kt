package com.example.inputtypes.input.currency

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.inputtypes.R
import com.example.inputtypes.databinding.CurrencyInputDoubleBinding

class CurrencyInputDouble @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CurrencyInputDoubleBinding.inflate(LayoutInflater.from(context), this, true)

    private lateinit var textChangeAnimation : Animation
    private var dotSeparator : CurrencySeparator = CurrencySeparator.DOT

    var onInputChanged : (String) -> Unit = {}

    private val watcher = object : TextWatcher{
        override fun beforeTextChanged(content: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(content: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(content: Editable?) {
            binding.textContainer.apply {
                text = getCurrentText()
                startAnimation(textChangeAnimation)
            }
            onInputChanged.invoke(getCurrentText())
        }
    }

    init {
        with(binding){
            val typedArray : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CurrencyInputDouble, 0,0)
            val defaultText = typedArray.getString(R.styleable.CurrencyInputDouble_defaultText) ?: INITIAL_PRIMARY_VALUE
            val animationId = typedArray.getResourceId(R.styleable.CurrencyInputDouble_onChangeAnimation, 0)
            dotSeparator = CurrencySeparator.values()[typedArray.getInt(R.styleable.CurrencyInputDouble_dotSeparator,0)]
            textChangeAnimation = AnimationUtils.loadAnimation(context, animationId)
            primaryValue.addTextChangedListener(watcher)
            typedArray.recycle()
        }
    }

    fun getCurrentText() : String {
        return binding.primaryValue.text.toString().replace(DOT, if(dotSeparator == CurrencySeparator.DOT) DOT else COMMA)
    }

    companion object {
        private const val INITIAL_PRIMARY_VALUE = "0.00"
        private const val COMMA = ","
        private const val DOT = "."
    }

}

enum class CurrencySeparator {DOT, COMMA}

