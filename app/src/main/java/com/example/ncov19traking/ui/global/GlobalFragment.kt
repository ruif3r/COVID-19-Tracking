package com.example.ncov19traking.ui.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.AlertDialogBuilder
import com.example.ncov19traking.BaseApp
import com.example.ncov19traking.R
import com.example.ncov19traking.models.ErrorBody
import com.example.ncov19traking.models.NCoVInfo
import com.example.ncov19traking.ui.settings.SettingsActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class GlobalFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val homeViewModel by activityViewModels<GlobalViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlobalFragmentViewHolder().progressBar.isVisible = true
        setupPieChart(GlobalFragmentViewHolder().pieChart)
        GlobalFragmentViewHolder().pieChart.isVisible = false
        setupObserverSubscription()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appbar_home_refresh -> {
                homeViewModel.deleteAll()
                return true
            }
            R.id.appbar_home_about -> {
                val aboutDialog = AlertDialogBuilder()
                activity?.let {
                    aboutDialog.aboutBuilder(it)
                }
            }
            R.id.appbar_settings -> startActivity(Intent(activity, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObserverSubscription() {
        homeViewModel.nCoVAllCases.observe(viewLifecycleOwner, Observer {
            with(GlobalFragmentViewHolder()) {
                totalCases.text = formatLargeNumbers(it.cases)
                recovered.text = formatLargeNumbers(it.recovered)
                deaths.text = formatLargeNumbers(it.deaths)
                lastUpdateLong.text = Date(it.updated).toString()
                casesPercentage.text = getString(
                    R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                        it.cases,
                        homeViewModel.nCoVYesterdayAllCases.cases
                    )
                )
                recoveredPercentage.text = getString(
                    R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                        it.recovered,
                        homeViewModel.nCoVYesterdayAllCases.recovered
                    )
                )
                deathsPercentage.text = getString(
                    R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                        it.deaths,
                        homeViewModel.nCoVYesterdayAllCases.deaths
                    )
                )
                pieChart.data = getChartData(it)
                pieChart.isVisible = true
                progressBar.isVisible = false
            }
        })
        homeViewModel.getErrorOnFetchFailure().observe(viewLifecycleOwner, Observer { error ->
            showErrorMessage(error)
        })
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

    private fun getChartData(nCoVInfo: NCoVInfo): PieData {
        val pieEntry = ArrayList<PieEntry>()
        pieEntry.add(PieEntry(nCoVInfo.cases.toFloat()))
        pieEntry.add(PieEntry(nCoVInfo.recovered.toFloat()))
        pieEntry.add(PieEntry(nCoVInfo.deaths.toFloat()))
        return PieData(PieDataSet(pieEntry, getString(R.string.piechart_dataset_label)).apply {
            colors = setChartColors()
            setDrawValues(false)
            sliceSpace = 2F
        })
    }

    private fun setChartColors(): List<Int> {
        val colors = ArrayList<Int>(3)
        colors.add(ContextCompat.getColor(requireContext(), R.color.textColor))
        colors.add(ContextCompat.getColor(requireContext(), R.color.recoveredColor))
        colors.add(ContextCompat.getColor(requireContext(), R.color.deathsColor))
        return colors
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as BaseApp).applicationComponent.inject(this)
    }


    private fun showErrorMessage(error: ErrorBody) {
        Toast.makeText(context, "Error ${error.code}: ${error.message}", Toast.LENGTH_LONG).show()
    }

    inner class GlobalFragmentViewHolder {
        private val view = requireView()
        val totalCases: TextView = view.findViewById(R.id.total_cases)
        val recovered: TextView = view.findViewById(R.id.recovered_numbers)
        val deaths: TextView = view.findViewById(R.id.deaths_numbers)
        val casesPercentage: TextView = view.findViewById(R.id.percentageCasesDifference)
        val recoveredPercentage: TextView = view.findViewById(R.id.percentageRecoveredDifference)
        val deathsPercentage: TextView = view.findViewById(R.id.percentageDeathsDifference)
        val lastUpdateLong: TextView = view.findViewById(R.id.last_update_date)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val pieChart: PieChart = view.findViewById(R.id.circular_chart)
    }
}

fun formatLargeNumbers(value: Int): String {
    val df = DecimalFormat("###,###,###")
    return df.format(value)
}
