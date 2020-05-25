package com.example.ncov19traking.ui.graphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.R
import com.example.ncov19traking.models.ErrorBody
import com.example.ncov19traking.models.Timeline
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class GraphsFragment : Fragment() {

    private val graphsViewModel by lazy {
        ViewModelProvider(this).get(GraphsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chart: LineChart = view.findViewById(R.id.chart)
        val textColor = ContextCompat.getColor(view.context, R.color.graphTextColor)
        setUpChart(chart, textColor)
        setupObservables(chart)
    }

    private fun setupObservables(chart: LineChart) {
        graphsViewModel.nCoVAllHistoricalData.observe(viewLifecycleOwner, Observer { nCovTimeline ->
            chart.data = getLineData(nCovTimeline)
            chart.invalidate()
        })
        graphsViewModel.getErrorOnFetchFailure().observe(viewLifecycleOwner, Observer { error ->
            showErrorMessage(error)
        })
    }

    private fun getLineData(nCovTimeline: Timeline?): LineData {
        val allLineData = ArrayList<ILineDataSet>()
        nCovTimeline?.run {
            allLineData.addDefinedDataset(
                nCovTimeline.cases,
                R.string.global_cases_string,
                R.color.colorAccent
            )
            allLineData.addDefinedDataset(
                nCovTimeline.deaths,
                R.string.total_deaths,
                R.color.deathsColor
            )
            allLineData.addDefinedDataset(
                nCovTimeline.recovered,
                R.string.total_recovered,
                R.color.recoveredColor
            )
        }
        return LineData(allLineData)
    }

    private fun ArrayList<ILineDataSet>.addDefinedDataset(
        cases: LinkedHashMap<String, Int>,
        messageStringResource: Int,
        colorResource: Int
    ) {
        add(
            defineDataSet(
                addDataToEntriesArrays(cases),
                getString(messageStringResource),
                ContextCompat.getColor(requireContext(), colorResource)
            )
        )
    }

    private fun showErrorMessage(error: ErrorBody) {
        Toast.makeText(context, "Error ${error.code}: ${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun setUpChart(chart: LineChart, textColor: Int) {
        chart.animateX(2000)
        chart.description.text = getString(R.string.global_graph_description)
        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = textColor
        chart.xAxis.textColor = textColor
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.setMaxVisibleValueCount(30)
    }

    private fun addDataToEntriesArrays(nCovTimelineData: LinkedHashMap<String, Int>): ArrayList<Entry> {
        val lineEntries = ArrayList<Entry>()
        var index = 0
        for (i in nCovTimelineData) {
            lineEntries.add(Entry(index.toFloat(), i.value.toFloat()))
            index++
        }
        return lineEntries
    }

    private fun defineDataSet(entry: ArrayList<Entry>, label: String, color: Int): LineDataSet {
        val dataSet = LineDataSet(entry, label)
        dataSet.valueTextColor = ResourcesCompat.getColor(resources, R.color.graphTextColor, null)
        dataSet.color = color
        dataSet.setCircleColor(color)
        dataSet.setCircleColorHole(color)
        return dataSet
    }
}
