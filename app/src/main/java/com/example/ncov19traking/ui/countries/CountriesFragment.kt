package com.example.ncov19traking.ui.countries

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ncov19traking.AlertDialogBuilder
import com.example.ncov19traking.NCoVRecyclerAdapter
import com.example.ncov19traking.R
import com.example.ncov19traking.models.ErrorBody

class CountriesFragment : Fragment() {

    private val countriesViewModel by activityViewModels<CountriesViewModel>()
    private val nCoVRecyclerAdapter = NCoVRecyclerAdapter()
    private var isSortedByCases = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CountriesViewHolder().progressBarCountry.isVisible = true
        setupRecyclerView()
        setupObserverSubscriptions()
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
                countriesViewModel.refreshCountryList()
                return true
            }
            R.id.appbar_sort -> {
                isSortedByCases = !isSortedByCases
                nCoVRecyclerAdapter.changeSort(isSortedByCases)
            }
            R.id.appbar_about -> {
                val aboutDialog = AlertDialogBuilder()
                activity?.let { aboutDialog.aboutBuilder(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        with(CountriesViewHolder()) {
            nCoVRecyclerAdapter.setHasStableIds(true)
            recyclerView.adapter = nCoVRecyclerAdapter
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }
    }

    private fun setupObserverSubscriptions() {
        countriesViewModel.nCoVCasesByCountry.observe(viewLifecycleOwner, Observer {
            nCoVRecyclerAdapter.addToListCountries(it as Array)
            CountriesViewHolder().progressBarCountry.isVisible = false
        })
        countriesViewModel.getErrorOnFetchFailure().observe(viewLifecycleOwner, Observer { error ->
            showErrorMessage(error)
        })
    }

    private fun showErrorMessage(error: ErrorBody) {
        Toast.makeText(context, "Error ${error.code}: ${error.message}", Toast.LENGTH_LONG).show()
    }

    inner class CountriesViewHolder {
        private val view = requireView()
        val recyclerView: RecyclerView = view.findViewById(R.id.country_recyclerView)
        val progressBarCountry: ProgressBar = view.findViewById(R.id.progressBarCountryList)
    }
}
