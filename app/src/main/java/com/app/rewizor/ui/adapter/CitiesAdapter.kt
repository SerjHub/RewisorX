package com.app.rewizor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.rewizor.R
import com.app.rewizor.data.model.Region
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_city.view.*

class CitiesAdapter(
    private val listOfCities: List<Region>,
    private val clickListener: (Int) -> Unit
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
            .let {
                CityViewHolder(it)
            }


    override fun getItemCount() = listOfCities.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CityViewHolder).bind(listOfCities[position], clickListener)
    }

    class CityViewHolder(
        override val containerView: View
    ) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer
    {
        fun bind(item: Region, clickListener: (id: Int) -> Unit) {
            with(containerView) {
               // firstSign.isInvisible = !item.isFirst
                firstSign.text = "${item.name.first().toUpperCase()}"
                fullCityName.text = item.name
                setOnClickListener { clickListener(item.id) }
            }
        }
    }
}