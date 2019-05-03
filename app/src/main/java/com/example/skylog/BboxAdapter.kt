package com.example.skylog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.skylog.data.Bbox_table

class BboxAdapter : RecyclerView.Adapter<BboxAdapter.ViewHolder>(){

        val data = mutableListOf<Bbox_table>()

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            val inflater = LayoutInflater.from(p0.context)
            val view = inflater.inflate(R.layout.bbox_layout, p0, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            p0.itemView.findViewById<TextView>(R.id.time).text = data[p1].params
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }