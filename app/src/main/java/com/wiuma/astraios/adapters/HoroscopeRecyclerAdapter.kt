package com.wiuma.astraios.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.wiuma.astraios.R
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.ui.horoscope.HoroscopeFragmentDirections
import com.wiuma.astraios.utilities.createPlaceholder
import com.wiuma.astraios.utilities.fetchImage
import kotlinx.android.synthetic.main.horoscope_recycler_row.view.*

class HoroscopeRecyclerAdapter(val horoscopeList: ArrayList<Horoscope>) :
        RecyclerView.Adapter<HoroscopeRecyclerAdapter.HoroscopeViewHolder>() {
    class HoroscopeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.horoscope_recycler_row, parent, false)
        return HoroscopeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        holder.itemView.horoscopeName.text = horoscopeList[position].nameHoroscope
        holder.itemView.horoscopeDates.text =
                horoscopeList.get(position).startDate + " - " + horoscopeList[position].endDate
        holder.itemView.horoscopeSign.fetchImage((horoscopeList[position].signHoroscope), createPlaceholder(holder.itemView.context))
        holder.itemView.setOnClickListener {
            val action =
                    HoroscopeFragmentDirections.actionNavigationHoroscopeToNavigationHoroscopeDetails(horoscopeList.get(position).Id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return horoscopeList.size
    }

    fun updateList(newHoroscopeList: List<Horoscope>) {
        horoscopeList.clear()
        horoscopeList.addAll(newHoroscopeList)
        notifyDataSetChanged()
    }
}