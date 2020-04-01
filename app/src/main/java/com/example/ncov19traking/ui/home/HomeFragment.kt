package com.example.ncov19traking.ui.home

import android.app.AlertDialog
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

class HomeFragment : Fragment() {

    private val homeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
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
        val lastUpdateLong: TextView = root.findViewById(R.id.last_update_date)
        progressBar = root.findViewById(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        homeViewModel.nCoVAllCases.observe(viewLifecycleOwner, Observer {
            totalCases.text = it.cases.toString()
            recovered.text = it.recovered.toString()
            deaths.text = it.deaths.toString()
            lastUpdateLong.text = Date(it.updated).toString()
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
                homeViewModel.refreshAllCases()
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
