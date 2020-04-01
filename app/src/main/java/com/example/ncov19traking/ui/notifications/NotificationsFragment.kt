package com.example.ncov19traking.ui.notifications

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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class NotificationsFragment : Fragment() {

    private val notificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
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
        notificationsViewModel.nCoVAllHistoricalData.observe(viewLifecycleOwner, Observer { nCovTimeline ->
          val lineEntriesCases = ArrayList<Entry>()
            val lineEntriesDeaths = ArrayList<Entry>()
            val lineEntriesRecovered = ArrayList<Entry>()
            var index = 0
            for (i  in nCovTimeline.cases){
                lineEntriesCases.add(Entry(index.toFloat(), i.value.toFloat()))
                index++
            }
            index = 0
            for (i  in  nCovTimeline.deaths){
                lineEntriesDeaths.add(Entry(index.toFloat(), i.value.toFloat()))
                index++
            }
            index = 0
            for (i  in  nCovTimeline.recovered){
                lineEntriesRecovered.add(Entry(index.toFloat(), i.value.toFloat()))
                index++
            }
            val allLineData = ArrayList<ILineDataSet>()
            allLineData.add(defineDataSet(lineEntriesCases, "Global Cases", "#6200EE"))
            allLineData.add(defineDataSet(lineEntriesDeaths, "Total Deaths", "#B71C1C"))
            allLineData.add(defineDataSet(lineEntriesRecovered, "Total Recovered", "#4CAF50"))
            val lineData = LineData(allLineData)
            chart.data = lineData
            chart.invalidate()
        })
        return root
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
