package com.hackathon.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.project1.data.model.History
import kotlinx.android.synthetic.main.history_cell.view.*

class HistoryAdapter (private val list: List<History>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_cell, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.nameTV.text = "Driver Name: "+data.driverName
        holder.priceTV.text = "Cost: "+data.cost + " "+ "KWD"
        holder.plateTV.text =  "Driver License: "+data.licensePlate
        holder.statusTV.text = data.status
//        if (data.status.equals( "Completed", ignoreCase = true)) {
//            bg.
//        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.tvName
        val priceTV: TextView = itemView.tvPrice
        val plateTV: TextView = itemView.tvPlate
        val statusTV: TextView = itemView.textViewStatus
        val bg: LinearLayout = itemView.statusBG
    }

}