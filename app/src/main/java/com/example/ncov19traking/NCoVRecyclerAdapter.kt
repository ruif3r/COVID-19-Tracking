package com.example.ncov19traking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ncov19traking.models.NumbersByCountry
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class NCoVRecyclerAdapter : RecyclerView.Adapter<NCoVRecyclerAdapter.ViewHolder>(), Filterable {

    private val dataset = ArrayList<NumbersByCountry>()

    private var datasetForSearch = dataset

    private var countryFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = datasetForSearch.takeIf { constraint.isNullOrEmpty() } ?: let {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    datasetForSearch.filter { it.country.contains(filterPattern, true) }
                }
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataset.clear()
            dataset.addAll(results?.values as ArrayList<NumbersByCountry>)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long = dataset[position].hashCode().toLong()

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nCoVInfo = dataset[position]
        Picasso.get().load(nCoVInfo.countryInfo.flag).into(holder.flag)
        holder.countryName.text = nCoVInfo.country
        holder.totalCases.text = nCoVInfo.cases.toString()
        holder.recovered.text =
            "${nCoVInfo.recovered} (${calculatePercentage(nCoVInfo.cases, nCoVInfo.recovered)}%)"
        holder.deaths.text =
            "${nCoVInfo.deaths} (${calculatePercentage(nCoVInfo.cases, nCoVInfo.deaths)}%)"
        holder.active.text = nCoVInfo.active.toString()
        holder.todayCases.text = nCoVInfo.todayCases.toString()
        holder.todayDeaths.text = nCoVInfo.todayDeaths.toString()
        holder.critical.text = nCoVInfo.critical.toString()

    }

    override fun getFilter() = countryFilter

    fun addToListCountries(countryList: Array<NumbersByCountry>?) {
        if (countryList != null) {
            dataset.addAll(countryList)
            sortByActiveCases()
            datasetForSearch = ArrayList(dataset)
            notifyDataSetChanged()
        }
    }

    private fun calculatePercentage(universeNumber: Int, fieldNumber: Int): String {
        return fieldNumber.times(100).div(universeNumber.toFloat()).format(2)
    }

    fun Float.format(digits: Int) = "%.${digits}f".format(this)

    private fun sortByActiveCases() {
        dataset.sortByDescending { it.active }
    }

    private fun sortAlphabetically() {
        dataset.sortBy { it.country }
    }

    fun changeSort(isSortedByCases: Boolean) {
        if (isSortedByCases) {
            sortByActiveCases()
        } else {
            sortAlphabetically()
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryName = itemView.findViewById<TextView>(R.id.countryName)
        var totalCases = itemView.findViewById<TextView>(R.id.total_cases_numbers)
        var deaths = itemView.findViewById<TextView>(R.id.deaths_cases_numbers)
        var recovered = itemView.findViewById<TextView>(R.id.recovered_cases_numbers)
        var active = itemView.findViewById<TextView>(R.id.active_cases_numbers)
        var todayCases = itemView.findViewById<TextView>(R.id.today_cases_numbers)
        var todayDeaths = itemView.findViewById<TextView>(R.id.today_deaths_numbers)
        var critical = itemView.findViewById<TextView>(R.id.critical_cases_numbers)
        var flag = itemView.findViewById<ImageView>(R.id.countryFlagImgeView)

    }
}
