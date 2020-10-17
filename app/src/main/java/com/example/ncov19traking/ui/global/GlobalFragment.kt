package com.example.ncov19traking.ui.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.AlertDialogBuilder
import com.example.ncov19traking.BaseApp
import com.example.ncov19traking.PieChartBuilder
import com.example.ncov19traking.R
import com.example.ncov19traking.models.ErrorBody
import com.example.ncov19traking.ui.CovidCustomCard
import com.example.ncov19traking.ui.settings.SettingsActivity
import com.example.ncov19traking.utils.formatLargeNumbers
import java.util.*
import javax.inject.Inject

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
        return inflater.inflate(R.layout.fragment_global, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlobalFragmentViewHolder().progressBar.isVisible = true
        GlobalFragmentViewHolder().pieChart.setVisibility(false)
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
                covidTotalCasesCard.setCaseNumbers(formatLargeNumbers(it.cases))
                covidRecoveredCard.setCaseNumbers(formatLargeNumbers(it.recovered))
                covidDeathsCard.setCaseNumbers(formatLargeNumbers(it.deaths))
                lastUpdateLong.text = Date(it.updated).toString()
                covidTotalCasesCard.setPercentageText(
                    getString(
                        R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                            it.cases,
                            homeViewModel.nCoVYesterdayAllCases.cases
                        )
                    )
                )
                covidRecoveredCard.setPercentageText(
                    getString(
                        R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                            it.recovered,
                            homeViewModel.nCoVYesterdayAllCases.recovered
                        )
                    )
                )
                covidDeathsCard.setPercentageText(
                    getString(
                        R.string.since_yesterday, homeViewModel.getCasesPercentageDifference(
                            it.deaths,
                            homeViewModel.nCoVYesterdayAllCases.deaths
                        )
                    )
                )
                pieChart.getChartData(it)
                pieChart.setVisibility(true)
                progressBar.isVisible = false
            }
        })
        homeViewModel.getErrorOnFetchFailure().observe(viewLifecycleOwner, Observer { error ->
            showErrorMessage(error)
        })
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
        val covidTotalCasesCard: CovidCustomCard = view.findViewById(R.id.covid_total_cases_card)
        val covidRecoveredCard: CovidCustomCard = view.findViewById(R.id.covid_recovered_cases_card)
        val covidDeathsCard: CovidCustomCard = view.findViewById(R.id.covid_deaths_cases_card)
        val lastUpdateLong: TextView = view.findViewById(R.id.last_update_date)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val pieChart = PieChartBuilder(view.findViewById(R.id.circular_chart))
    }
}
