package com.example.ncov19traking.ui.graphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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

    private val graphsViewModel by activityViewModels<GraphsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chart: LineChart = view.findViewById(R.id.chart)
        val progressBar: ProgressBar = view.findViewById(R.id.graph_fragment_progressBar)
        val textColor = ContextCompat.getColor(view.context, R.color.graphTextColor)
        setUpChart(chart, textColor)
        chart.isVisible = false
        setupObserverSubscriptions(progressBar, chart)
    }

    private fun setupObserverSubscriptions(
        progressBar: ProgressBar,
        chart: LineChart
    ) {
        graphsViewModel.nCoVAllHistoricalData.observe(viewLifecycleOwner, Observer { nCovTimeline ->
            progressBar.isVisible = true
            chart.data = getLineData(nCovTimeline)
            chart.invalidate()
            chart.isVisible = true
            progressBar.isVisible = false
        })
        graphsViewModel.getErrorOnFetchFailure().observe(viewLifecycleOwner, Observer { error ->
            showErrorMessage(error)
        })
    }

    private fun getLineData(
        nCovTimeline: Timeline?
    ): LineData? {
        nCovTimeline?.let {
            val allLineData = ArrayList<ILineDataSet>()
            allLineData.add(
                defineDataSet(
                    addDataToEntriesArrays(nCovTimeline.cases),
                    getString(R.string.global_cases_string),
                    ContextCompat.getColor(requireContext(), R.color.colorAccent)
                )
            )
            allLineData.add(
                defineDataSet(
                    addDataToEntriesArrays(nCovTimeline.deaths),
                    getString(R.string.total_deaths),
                    ContextCompat.getColor(requireContext(), R.color.deathsColor)
                )
            )
            allLineData.add(
                defineDataSet(
                    addDataToEntriesArrays(nCovTimeline.recovered),
                    getString(R.string.total_recovered),
                    ContextCompat.getColor(requireContext(), R.color.recoveredColor)
                )
            )
            return LineData(allLineData)
        }
        return null
    }

    private fun showErrorMessage(error: ErrorBody) {
        Toast.makeText(context, "Error ${error.code}: ${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun setUpChart(chart: LineChart, textColor: Int) {
        chart.apply {
            animateX(2000)
            description.text = getString(R.string.global_graph_description)
            axisRight.isEnabled = false
            axisLeft.textColor = textColor
            xAxis.textColor = textColor
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setMaxVisibleValueCount(30)
        }
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
        return LineDataSet(entry, label).apply {
            valueTextColor = ResourcesCompat.getColor(resources, R.color.graphTextColor, null)
            this.color = color
            setCircleColor(color)
            setCircleColorHole(color)
        }
    }
}
