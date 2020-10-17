package com.example.ncov19traking.utils

import java.text.DecimalFormat

class NumberFormatter

fun formatLargeNumbers(value: Int): String {
    val df = DecimalFormat("###,###,###")
    return df.format(value)
}
