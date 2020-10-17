package com.example.ncov19traking.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ncov19traking.R
import com.google.android.material.card.MaterialCardView

const val DEFAULT_TITLE_TEXT = "Title"
const val DEFAULT_NUMBER_TEXT = "0"
const val DEFAULT_PERCENTAGE_TEXT = "0%"
const val CARD_ELEVATION = 10F
const val CARD_RADIUS = 16F

class CovidCustomCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var titleText = DEFAULT_TITLE_TEXT
    private var numberText = DEFAULT_NUMBER_TEXT
    private var percentageText = DEFAULT_PERCENTAGE_TEXT
    private var numberTextColor = resources.getColor(R.color.recoveredColor)
    private var percentageTextColor = resources.getColor(R.color.textColor)
    private lateinit var titleTextView : TextView
    private lateinit var numbersTextView : TextView
    private lateinit var percentageTextView : TextView

    init {
        setupCard()
        context.theme.obtainStyledAttributes(attrs, R.styleable.CovidCustomCard, defStyleAttr, 0).apply {
            try {

                titleText = getString(R.styleable.CovidCustomCard_titleText) ?: DEFAULT_TITLE_TEXT
                numberText = getString(R.styleable.CovidCustomCard_caseNumberText) ?: DEFAULT_NUMBER_TEXT
                percentageText = getString(R.styleable.CovidCustomCard_percentageText) ?: DEFAULT_PERCENTAGE_TEXT
                numberTextColor = getColor(R.styleable.CovidCustomCard_caseNumberTextColor, numberTextColor)
                percentageTextColor = getColor(R.styleable.CovidCustomCard_percentageTextColor, percentageTextColor)

            } finally {
                recycle()
            }

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.covid_custom_card, this@CovidCustomCard)
        }
    }

    fun setTitleText(newTitle : String){
        titleTextView.text = newTitle
    }

    fun setCaseNumbers(cases : String){
        numbersTextView.text = cases
    }

    fun setPercentageText(newPercentageText : String){
        percentageTextView.text = newPercentageText
    }

    private fun setupCard(){
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundColor))
        cardElevation = CARD_ELEVATION
        radius = CARD_RADIUS
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        titleTextView = findViewById<TextView>(R.id.custom_card_title).apply {
            text = titleText
        }
        numbersTextView = findViewById<TextView>(R.id.custom_card_numbers).apply {
            text = numberText
            setTextColor(numberTextColor)
        }
        percentageTextView = findViewById<TextView>(R.id.custom_card_percentage).apply {
            text = percentageText
            setTextColor(percentageTextColor)
        }
    }
}