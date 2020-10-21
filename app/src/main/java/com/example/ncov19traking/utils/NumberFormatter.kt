package com.example.ncov19traking.utils

import java.text.DecimalFormat

object NumberFormatter {

    fun Int.formatLargeNumbers(): String {
        val df = DecimalFormat("###,###,###")
        return df.format(this)
    }
}

