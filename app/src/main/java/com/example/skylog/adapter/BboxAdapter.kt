package com.example.skylog.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.skylog.R
import com.example.skylog.data.Bbox_line
import java.util.ArrayList

class BboxAdapter(private val context: Context, var Bbox_List: MutableList<Bbox_line>) :
    RecyclerView.Adapter<BboxAdapter.BboxViewHolder>()/*,
    Filterable */{
   // private var filteredBboxList: List<Bbox_line>? = null

  /*  init {
        this.filteredBboxList = Bbox_List
    }*/

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            TYPE_ROW_COLORFUL
        } else TYPE_ROW

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BboxViewHolder {
        if (viewType == TYPE_ROW) {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment2_bbox_row, viewGroup, false)
            return BboxViewHolder(view)
        } else {
            val view = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.fragment2_bbox_row_dark,
                viewGroup, false
            )
            return BboxViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BboxViewHolder, position: Int) {
        val Bbox_line:Bbox_line=Bbox_List[Bbox_List.count()-position-1]

        holder.val_wind.setText(Bbox_line.wind.toString())
        holder.val_outs6_13.setText(Bbox_line.outs6_13.toString())
        holder.val_operatorintervent.setText(if(Bbox_line.operatorintervent)"ON" else "OFF")
        holder.val_ugaz.setText(Bbox_line.ugaz.toString())
        holder.val_linkont.setText(if(Bbox_line.linkont)"ON" else "OFF")
        holder.val_zeroput.setText(if(Bbox_line.zeroput)"ON" else "OFF")
        holder.val_s.setText(Bbox_line.s.toString())
        holder.val_q.setText(Bbox_line.q.toString())
        holder.val_r.setText(Bbox_line.r.toString())
        holder.val_h.setText(Bbox_line.h.toString())
        holder.val_qm.setText(Bbox_line.qm.toString())
        holder.val_m.setText(Bbox_line.m.toString())
        holder.val_outs1_5.setText(Bbox_line.outs1_5.toString())
        holder.val_dpm_s.setText(Bbox_line.dpm_s.toString())
        holder.val_dpm_az.setText(Bbox_line.dpm_az.toString())
        holder.val_dpm_r.setText(Bbox_line.dpm_r.toString())
        holder.val_dpm_h.setText(Bbox_line.dpm_h.toString())
        holder.val_force_dat.setText(Bbox_line.force_dat.toString())
    }

    override fun getItemCount(): Int {
        return Bbox_List!!.size
    }

    inner class BboxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var val_wind                        : TextView
        var val_outs6_13                    : TextView
        var val_operatorintervent           : TextView
        var val_ugaz                        : TextView
        var val_linkont                     : TextView
        var val_zeroput                     : TextView
        var val_s                           : TextView
        var val_q                           : TextView
        var val_r                           : TextView
        var val_h                           : TextView
        var val_qm                          : TextView
        var val_m                           : TextView
        var val_outs1_5                     : TextView
        var val_dpm_s                       : TextView
        var val_dpm_az                      : TextView
        var val_dpm_r                       : TextView
        var val_dpm_h                       : TextView
        var val_force_dat                   : TextView


        init {
            val_wind                = view.findViewById(R.id.val_wind)
            val_outs6_13            = view.findViewById(R.id.val_outs6_13)
            val_operatorintervent   = view.findViewById(R.id.val_operatorintervent)
            val_ugaz                = view.findViewById(R.id.val_ugaz)
            val_linkont             = view.findViewById(R.id.val_linkont)
            val_zeroput             = view.findViewById(R.id.val_zeroput)
            val_s                   = view.findViewById(R.id.val_s)
            val_q                   = view.findViewById(R.id.val_q)
            val_r                   = view.findViewById(R.id.val_r)
            val_h                   = view.findViewById(R.id.val_h)
            val_qm                  = view.findViewById(R.id.val_qm)
            val_m                   = view.findViewById(R.id.val_m)
            val_outs1_5             = view.findViewById(R.id.val_outs1_5)
            val_dpm_s               = view.findViewById(R.id.val_dpm_s)
            val_dpm_az              = view.findViewById(R.id.val_dpm_az)
            val_dpm_r               = view.findViewById(R.id.val_dpm_r)
            val_dpm_h               = view.findViewById(R.id.val_dpm_h)
            val_force_dat           = view.findViewById(R.id.val_force_dat)

        }
    }

   /* override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredBboxList = Bbox_List
                } else {
                    val filteredList = ArrayList<Bbox_line>()
                    for (Bbox_line in Bbox_List) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                       // if (Bbox_List.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(Bbox_line)
                        //}
                    }

                    filteredBboxList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filteredBboxList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredBboxList = filterResults.values as ArrayList<Bbox_line>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }*/

    companion object {
        private val TYPE_ROW = 0
        private val TYPE_ROW_COLORFUL = 1
    }
}