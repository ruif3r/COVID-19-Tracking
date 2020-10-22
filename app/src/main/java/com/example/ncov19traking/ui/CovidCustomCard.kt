package com.example.ncov19traking.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ncov19traking.R
import com.google.android.material.card.MaterialCardView

private const val DEFAULT_NUMBER_TEXT = "0"
private const val DEFAULT_PERCENTAGE_TEXT = "0%"

class CovidCustomCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val defaultTitleText = resources.getString(R.string.custom_covid_card_default_title)
    private var titleText = defaultTitleText
    private var numberText = DEFAULT_NUMBER_TEXT
    private var percentageText = DEFAULT_PERCENTAGE_TEXT
    private var numberTextColor = resources.getColor(R.color.recoveredColor)
    private var percentageTextColor = resources.getColor(R.color.textColor)
    private var titleTextView: TextView? = null
    private var numbersTextView: TextView? = null
    private var percentageTextView: TextView? = null

    init {
        receiveAttributes(context, attrs, defStyleAttr)
        inflateView(context)
        initializeView()
    }

    fun setTitleText(newTitle: String) {
        titleTextView?.text = newTitle
    }

    fun setCaseNumbers(cases: String) {
        numbersTextView?.text = cases
    }

    fun setPercentageText(newPercentageText: String) {
        percentageTextView?.text = newPercentageText
    }

    private fun receiveAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CovidCustomCard, defStyleAttr, 0)
            .apply {
                try {

                    titleText =
                        getString(R.styleable.CovidCustomCard_titleText) ?: defaultTitleText
                    numberText =
                        getString(R.styleable.CovidCustomCard_caseNumberText) ?: DEFAULT_NUMBER_TEXT
                    percentageText = getString(R.styleable.CovidCustomCard_percentageText)
                        ?: DEFAULT_PERCENTAGE_TEXT
                    numberTextColor =
                        getColor(R.styleable.CovidCustomCard_caseNumberTextColor, numberTextColor)
                    percentageTextColor = getColor(
                        R.styleable.CovidCustomCard_percentageTextColor,
                        percentageTextColor
                    )

                } finally {
                    recycle()
                }
            }
    }

    private fun inflateView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.covid_custom_card, this@CovidCustomCard)
    }

    private fun initializeView() {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundColor))
        cardElevation = resources.getDimension(R.dimen.custom_covid_card_elevation)
        radius = resources.getDimension(R.dimen.custom_covid_card_radius)
        isClickable = true
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