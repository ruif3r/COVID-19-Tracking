package com.example.ncov19traking

import androidx.core.content.ContextCompat
import com.example.ncov19traking.models.NCoVInfo
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

object PieChartConfigHelper {

    fun setupPieChart(pieChart: PieChart) {
        pieChart.apply {
            animateY(1500)
            setDrawEntryLabels(false)
            setUsePercentValues(false)
            setHoleColor(ContextCompat.getColor(context, R.color.backgroundColor))
            description.isEnabled = false
            legend.isEnabled = false
        }
    }

    fun setAndConfigChartData(nCoVInfo: NCoVInfo, pieChart: PieChart, vararg colorList: Int) {
        val pieEntry = ArrayList<PieEntry>().apply {
            add(PieEntry((nCoVInfo.cases - nCoVInfo.recovered - nCoVInfo.deaths).toFloat()))
            add(PieEntry(nCoVInfo.recovered.toFloat()))
            add(PieEntry(nCoVInfo.deaths.toFloat()))
        }
        pieChart.data = PieData(
            PieDataSet(
                pieEntry,
                pieChart.context.getString(R.string.piechart_dataset_label)
            ).apply {
                colors = colorList.toList()
                setDrawValues(false)
                sliceSpace = 2F
            })
    }
}