package com.example.ncov19traking.ui.countries

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ncov19traking.AlertDialogBuilder
import com.example.ncov19traking.NCoVRecyclerAdapter
import com.example.ncov19traking.R

class CountriesFragment : Fragment() {

    private val dashboardViewModel by lazy {
        ViewModelProvider(this).get(CountriesViewModel::class.java)
    }
    private val nCoVRecyclerAdapter = NCoVRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.country_recyclerView)
        val progressBarCountry = root.findViewById<ProgressBar>(R.id.progressBarCountryList)
        progressBarCountry.visibility = ProgressBar.VISIBLE
        recyclerView.adapter = nCoVRecyclerAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        dashboardViewModel.nCoVCasesByCountry.observe(viewLifecycleOwner, Observer {
            nCoVRecyclerAdapter.addToListCountries(it as Array)
            progressBarCountry.visibility = ProgressBar.GONE
        })

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.appbar_search)
        val searchView = searchItem?.actionView as SearchView?

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                nCoVRecyclerAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appbar_refresh -> {
                dashboardViewModel.refreshCountryList()
                return true
            }
            R.id.appbar_about -> {
                val aboutDialog = AlertDialogBuilder()
                activity?.let { aboutDialog.aboutBuilder(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
