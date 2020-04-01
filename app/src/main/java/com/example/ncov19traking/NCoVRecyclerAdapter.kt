package com.example.ncov19traking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ncov19traking.models.NumbersByCountry
import java.util.*
import kotlin.collections.ArrayList

class NCoVRecyclerAdapter : RecyclerView.Adapter<NCoVRecyclerAdapter.ViewHolder>(), Filterable {

    private val dataset = ArrayList<NumbersByCountry>()
    private var datasetForSearch = dataset

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nCoVInfo = dataset[position]
        holder.countryName.text = nCoVInfo.country
        holder.totalCases.text = nCoVInfo.cases.toString()
        holder.recovered.text = nCoVInfo.recovered.toString()
        holder.deaths.text = nCoVInfo.deaths.toString()
        holder.active.text = nCoVInfo.active.toString()
        holder.todayCases.text = nCoVInfo.todayCases.toString()
        holder.todayDeaths.text = nCoVInfo.todayDeaths.toString()
        holder.critical.text = nCoVInfo.critical.toString()

    }

    fun addToListCountries(countryList: ArrayList<NumbersByCountry>?) {
        if (countryList != null) {
            dataset.addAll(countryList)
            datasetForSearch = ArrayList(dataset)
            notifyDataSetChanged()
        }
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryName = itemView.findViewById<TextView>(R.id.countryName)
        var totalCases = itemView.findViewById<TextView>(R.id.total_cases_numbers)
        var deaths = itemView.findViewById<TextView>(R.id.deaths_cases_numbers)
        var recovered = itemView.findViewById<TextView>(R.id.recovered_cases_numbers)
        var active = itemView.findViewById<TextView>(R.id.active_cases_numbers)
        var todayCases = itemView.findViewById<TextView>(R.id.today_cases_numbers)
        var todayDeaths = itemView.findViewById<TextView>(R.id.today_deaths_numbers)
        var critical = itemView.findViewById<TextView>(R.id.critical_cases_numbers)
    }

    override fun getFilter() = countryFilter

    private var countryFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = ArrayList<NumbersByCountry>()
            if (constraint.isNullOrEmpty()){
                filteredList = datasetForSearch
            }else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (item in datasetForSearch){
                    if (item.country.toLowerCase(Locale.ROOT).contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val result = FilterResults()
            result.values=filteredList
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataset.clear()
            dataset.addAll(results?.values as ArrayList<NumbersByCountry>)
            notifyDataSetChanged()
        }

    }
}