package com.example.ncov19traking.ui.global

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ncov19traking.AlertDialogBuilder
import com.example.ncov19traking.R
import java.util.*

class GlobalFragment : Fragment() {

    private val homeViewModel by lazy {
        ViewModelProvider(this).get(GlobalViewModel::class.java)
    }
    private lateinit var progressBar : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val totalCases: TextView = root.findViewById(R.id.total_cases)
        val recovered: TextView = root.findViewById(R.id.recovered_numbers)
        val deaths: TextView = root.findViewById(R.id.deaths_numbers)
        val casesPercentage: TextView = root.findViewById(R.id.percentageCasesDifference)
        val recoveredPercentage: TextView = root.findViewById(R.id.percentageRecoveredDifference)
        val deathsPercentage: TextView = root.findViewById(R.id.percentageDeathsDifference)
        val lastUpdateLong: TextView = root.findViewById(R.id.last_update_date)
        progressBar = root.findViewById(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        homeViewModel.nCoVAllCases.observe(viewLifecycleOwner, Observer {
            totalCases.text = it.cases.toString()
            recovered.text = it.recovered.toString()
            deaths.text = it.deaths.toString()
            lastUpdateLong.text = Date(it.updated).toString()
                casesPercentage.text = "+${homeViewModel.getCasesPercentageDifference(it.cases, homeViewModel.nCoVYesterdayAllCases.cases)}% since yesterday"
                recoveredPercentage.text = "+${homeViewModel.getCasesPercentageDifference(it.recovered, homeViewModel.nCoVYesterdayAllCases.recovered)}% since yesterday"
                deathsPercentage.text = "+${homeViewModel.getCasesPercentageDifference(it.deaths, homeViewModel.nCoVYesterdayAllCases.deaths)}% since yesterday"
            progressBar.visibility = ProgressBar.GONE
        })

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.appbar_home_refresh -> {
                homeViewModel.deleteAll()
                return true
            }
            R.id.appbar_home_about -> {
                val aboutDialog = AlertDialogBuilder()
                activity?.let { aboutDialog.aboutBuilder(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
