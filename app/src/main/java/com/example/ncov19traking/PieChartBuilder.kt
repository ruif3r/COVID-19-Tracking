package com.example.ncov19traking

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.ncov19traking.models.NCoVInfo
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartBuilder(private val pieChart: PieChart) {

    init {
        setupPieChart(pieChart)
    }

    private fun setupPieChart(pieChart: PieChart) {
        pieChart.apply {
            animateY(1500)
            setDrawEntryLabels(false)
            setUsePercentValues(false)
            setHoleColor(ContextCompat.getColor(context, R.color.backgroundColor))
            description.isEnabled = false
            legend.isEnabled = false
        }
    }

    fun getChartData(nCoVInfo: NCoVInfo) {
        val pieEntry = ArrayList<PieEntry>()
        pieEntry.add(PieEntry(nCoVInfo.cases.toFloat()))
        pieEntry.add(PieEntry(nCoVInfo.recovered.toFloat()))
        pieEntry.add(PieEntry(nCoVInfo.deaths.toFloat()))
        pieChart.data = PieData(PieDataSet(pieEntry, pieChart.context.getString(R.string.piechart_dataset_label)).apply {
            colors = setChartColors()
            setDrawValues(false)
            sliceSpace = 2F
        })
    }

    private fun setChartColors(): List<Int> {
        val colors = ArrayList<Int>(3)
        colors.add(ContextCompat.getColor(pieChart.context, R.color.textColor))
        colors.add(ContextCompat.getColor(pieChart.context, R.color.recoveredColor))
        colors.add(ContextCompat.getColor(pieChart.context, R.color.deathsColor))
        return colors
    }

    fun setVisibility(isVisible : Boolean){
        pieChart.isVisible = isVisible
    }
}