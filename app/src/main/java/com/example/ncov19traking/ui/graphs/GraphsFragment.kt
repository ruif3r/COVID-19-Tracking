package com.example.ncov19traking.ui.graphs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GraphsFragment : Fragment() {

    private val notificationsViewModel by lazy {
        ViewModelProvider(this).get(GraphsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val chart: LineChart = root.findViewById(R.id.chart)
        chart.animateX(2000)
        chart.description.text = "COVID-19 Timeline"
        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = Color.parseColor("#9E9E9E")
        chart.xAxis.textColor = Color.parseColor("#9E9E9E")
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.setMaxVisibleValueCount(30)
        notificationsViewModel.nCoVAllHistoricalData.observe(viewLifecycleOwner, Observer { nCovTimeline ->
            val allLineData = ArrayList<ILineDataSet>()
            if (nCovTimeline != null) {
                allLineData.add(
                    defineDataSet(
                        addDataToEntriesArrays(nCovTimeline.cases),
                        "Global Cases",
                        "#6200EE"
                    )
                )
                allLineData.add(
                    defineDataSet(
                        addDataToEntriesArrays(nCovTimeline.deaths),
                        "Total Deaths",
                        "#B71C1C"
                    )
                )
                allLineData.add(
                    defineDataSet(
                        addDataToEntriesArrays(nCovTimeline.recovered),
                        "Total Recovered",
                        "#4CAF50"
                    )
                )
            }
            val lineData = LineData(allLineData)
            chart.data = lineData
            chart.invalidate()
        })
        return root
    }

    private fun addDataToEntriesArrays(nCovTimelineData : LinkedHashMap<String, Int>): ArrayList<Entry> {
        val lineEntries = ArrayList<Entry>()
        var index = 0
        for (i  in nCovTimelineData){
            lineEntries.add(Entry(index.toFloat(), i.value.toFloat()))
            index++
        }
        return lineEntries
    }

    private fun defineDataSet(entry: ArrayList<Entry>, label : String, color : String) : LineDataSet{
        val dataSet = LineDataSet(entry, label)
        dataSet.valueTextColor = Color.parseColor("#9E9E9E")
        dataSet.color = Color.parseColor(color)
        dataSet.setCircleColor(Color.parseColor(color))
        dataSet.setCircleColorHole(Color.parseColor(color))
        return dataSet
    }
}