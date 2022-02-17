package com.example.inputtypes.input.currency

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.core.text.set
import androidx.core.widget.addTextChangedListener
import com.example.inputtypes.R
import com.example.inputtypes.databinding.CurrencyInputDoubleBinding
import com.example.inputtypes.misc.ClipboardResolver

class CurrencyInputDouble @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CurrencyInputDoubleBinding.inflate(LayoutInflater.from(context), this, true)

    private var prevContent = ""
    private var startCursor = 0
    private var endCursor = 0
    private var isPasting = false

    var onInputChanged : (String) -> Unit = {}

    private val watcher = object : TextWatcher{
        override fun beforeTextChanged(content: CharSequence?, start: Int, count: Int, after: Int) {
            println(content)
            prevContent = content.toString()
            startCursor = start
            endCursor = start + count
            isPasting = after > 1
        }

        override fun onTextChanged(content: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(content: Editable?) {
            if(content.isNullOrEmpty()) setInitialValue()
            if(isPasting) manageClipboardContent()
            onInputChanged.invoke(getCurrentText())
        }
    }

    init {
        with(binding){
            primaryValue.addTextChangedListener(watcher)
        }
    }

    fun getCurrentText() : String {
        return binding.primaryValue.text.split(DOT).run {
            if (this.size > 1) {
                if(this[1].length > 1) "${this[0]},${this[1]}" else "${this[0]},0${this[1]}"
            }
            else{
                "${this[0]},00"
            }
        }
    }

    private fun manageClipboardContent(){
        binding.primaryValue.removeTextChangedListener(watcher)
        val clipboardText = ClipboardResolver.getClipboardText(context)
        var integerPart = ""
        var decimalPart = ""
        when {
            clipboardText.contains(COMMA) -> {
                val splittedWithComma = clipboardText.split(COMMA)
                integerPart = splittedWithComma[0]
                decimalPart = splittedWithComma[1]
            }
            clipboardText.contains(DOT) -> {
                val splittedWithDot = clipboardText.split(DOT)
                integerPart = splittedWithDot[0]
                decimalPart = splittedWithDot[1]
            }
            else -> {
                integerPart = clipboardText
            }
        }
        integerPart.filter { it.isDigit() }
        decimalPart.filter { it.isDigit() }
        val newText =
        if(startCursor != endCursor)
            prevContent.substring(0, startCursor) + integerPart + prevContent.substring(endCursor, prevContent.length)
        else
            prevContent.substring(0, startCursor) + integerPart + prevContent.substring(startCursor, prevContent.length)

        with(binding){
            val number = (newText+decimalPart).replace(COMMA, DOT).toDouble()
            primaryValue.setText(number.toString())
            primaryValue.addTextChangedListener(watcher)
        }
    }

    private fun setInitialValue() {
        binding.primaryValue.apply {
            removeTextChangedListener(watcher)
            setText(INITIAL_PRIMARY_VALUE)
            addTextChangedListener(watcher)
        }
    }

    companion object {
        private const val INITIAL_PRIMARY_VALUE = "0.00"
        private const val COMMA = ","
        private const val DOT = "."
    }

}